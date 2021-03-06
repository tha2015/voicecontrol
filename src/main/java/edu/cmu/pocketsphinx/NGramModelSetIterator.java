/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package edu.cmu.pocketsphinx;

public class NGramModelSetIterator implements java.util.Iterator<NGramModel> {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected NGramModelSetIterator(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(NGramModelSetIterator obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        SphinxBaseJNI.delete_NGramModelSetIterator(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  public NGramModelSetIterator(SWIGTYPE_p_void ptr) {
    this(SphinxBaseJNI.new_NGramModelSetIterator(SWIGTYPE_p_void.getCPtr(ptr)), true);
  }

  public NGramModel next() {
    long cPtr = SphinxBaseJNI.NGramModelSetIterator_next(swigCPtr, this);
    return (cPtr == 0) ? null : new NGramModel(cPtr, true);
  }

  public boolean hasNext() {
    return SphinxBaseJNI.NGramModelSetIterator_hasNext(swigCPtr, this);
  }

}
