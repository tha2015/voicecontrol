package org.freejava.voicecontrol;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.freejava.voicecontrol.impl.CommandManagerImpl;
import org.freejava.voicecontrol.impl.GoogleSpeechManagerImpl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.io.ByteStreams;

import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.Segment;

public class App
{
    public static void main( String[] args ) throws Exception
    {

        File newRoot = extract();

        loadJniLib(newRoot);

        new App().listen(newRoot);

        System.out.println( "Hello World!" );
    }

    private ICommandManager mgr = new CommandManagerImpl();

    public void listen(File newRoot) throws Exception {
        Config c = Decoder.defaultConfig();
        c.setString("-hmm", new File(newRoot, "model/en-us/en-us").getAbsolutePath());
        c.setString("-lm", new File(newRoot, "model/en-us/en-us.lm.bin").getAbsolutePath());
        c.setString("-dict", new File(newRoot, "model/en-us/cmudict-en-us.dict").getAbsolutePath());
        Decoder d = new Decoder(c);

        File temp = File.createTempFile("tempfile", ".txt");
        FileWriter fw = new FileWriter(temp);
        for (String keyword : new String[] {"lily", "alex"}) {
            fw.write(keyword + "\n");
        }
        fw.close();

        d.setKws("keyphrase_search", temp.getAbsolutePath());
        d.setSearch("keyphrase_search");

        TargetDataLine line = getAudioInputLine();
        line.start();

        d.startUtt();
        d.setRawdataSize(300000);

        byte[] b = new byte[4096];
        int nbytes;

        boolean inSpeech = false;
        while ((nbytes = line.read(b, 0, 4096)) >= 0) {
            ByteBuffer bb = ByteBuffer.wrap(b, 0, nbytes);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            short[] s = new short[nbytes/2];
            bb.asShortBuffer().get(s);
            d.processRaw(s, nbytes/2, false, false);

            boolean inSpeechNew = d.getInSpeech();
            if (!inSpeech && inSpeechNew) {
                System.out.println("Listening...");
                inSpeech = true;
            } else if (inSpeech && !inSpeechNew) {
                System.out.println("Stopping...");
                inSpeech = false;
                d.endUtt();
                final Hypothesis hypothesis = d.hyp();
                if (hypothesis != null) {
                    String hypstr = hypothesis.getHypstr();

                    String languageCode = "en-US";
                    if (hypstr.indexOf("lily") != -1) {
                        languageCode = "vi-VN";
                    }
                    System.out.println(languageCode + ":" + hypstr );

                    String text = new GoogleSpeechManagerImpl().transcript(languageCode, d.getRawdata());
                    mgr.handleCommand(text);

                }
                d.startUtt();

            } else {
                System.out.print(inSpeech?"+":"-");
            }

        }

        short[] data = d.getRawdata();
        System.out.println("Data size: " + data.length);
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("/tmp/test.raw")));
        for (int i = 0; i < data.length; i++) {
            dos.writeShort(data[i]);
        }
        dos.close();

        for (Segment seg : d.seg()) {
            System.out.println(seg.getWord());
        }
    }
    private static TargetDataLine getAudioInputLine() {
        final int BYTES_PER_SAMPLE = 2; // bytes per sample for LINEAR16

        final int samplingRate = 16000;
        int bytesPerBuffer = 4096; // buffer size in bytes

        AudioFormat format = new AudioFormat(samplingRate, BYTES_PER_SAMPLE * 8, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new RuntimeException(
                    String.format("Device doesn't support LINEAR16 mono raw audio format at {}Hz", samplingRate));
        }
        try {
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            // Make sure the line buffer doesn't overflow while we're filling
            // this thread's buffer.
            line.open(format, bytesPerBuffer * 5);
            return line;
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadJniLib(File newRoot) {
        Library.loadLibrary(new File(newRoot, "native"), "pocketsphinx_jni");
    }

    private static File extract() throws IOException {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String root = "/pocketsphinx";
        List<String> relPaths = new ArrayList<>();
        Map<String, Resource> resourceMap = new HashMap<>();
        for (Resource resource : resolver.getResources("classpath*:" + root + "/**/*")) {
            URL url = resource.getURL();
            String absPath = url.getPath();
            String relPath = absPath.substring(absPath.indexOf(root) + root.length() + 1);
            System.out.println(relPath);
            relPaths.add(relPath);
            resourceMap.put(relPath, resource);
        }
        Collections.sort(relPaths, (o1,o2)->o2.length() - o1.length());
        System.out.println(relPaths);

        File newRoot = Files.createTempDirectory("voicecontrol").toFile();
        for (String relPath : relPaths) {
            File newFile = new File(newRoot, relPath);
            if (!newFile.exists()) {
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                Resource source = resourceMap.get(relPath);
                if (source.contentLength() > 0) {
                    try (FileOutputStream os = new FileOutputStream(newFile); InputStream is = source.getInputStream()) {
                        ByteStreams.copy(is, os);
                    }
                }
            }
        }
        System.out.println(newRoot);
        return newRoot;

    }
}
