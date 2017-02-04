/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.cmu.pocketsphinx;

public class NGramModelSet implements Iterable<NGramModel> {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected NGramModelSet(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(NGramModelSet obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        SphinxBaseJNI.delete_NGramModelSet(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public NGramModelSetIterator iterator() {
    long cPtr = SphinxBaseJNI.NGramModelSet_iterator(swigCPtr, this);
    return (cPtr == 0) ? null : new NGramModelSetIterator(cPtr, true);
  }

  public NGramModelSet(Config config, LogMath logmath, String path) {
    this(SphinxBaseJNI.new_NGramModelSet(Config.getCPtr(config), config, LogMath.getCPtr(logmath), logmath, path), true);
  }

  public int count() {
    return SphinxBaseJNI.NGramModelSet_count(swigCPtr, this);
  }

  public NGramModel add(NGramModel model, String name, float weight, boolean reuse_widmap) {
    long cPtr = SphinxBaseJNI.NGramModelSet_add(swigCPtr, this, NGramModel.getCPtr(model), model, name, weight, reuse_widmap);
    return (cPtr == 0) ? null : new NGramModel(cPtr, false);
  }

  public NGramModel select(String name) {
    long cPtr = SphinxBaseJNI.NGramModelSet_select(swigCPtr, this, name);
    return (cPtr == 0) ? null : new NGramModel(cPtr, false);
  }

  public NGramModel lookup(String name) {
    long cPtr = SphinxBaseJNI.NGramModelSet_lookup(swigCPtr, this, name);
    return (cPtr == 0) ? null : new NGramModel(cPtr, false);
  }

  public String current() {
    return SphinxBaseJNI.NGramModelSet_current(swigCPtr, this);
  }

}