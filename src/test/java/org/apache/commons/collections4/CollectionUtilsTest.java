package org.apache.commons.collections4;
/*Teste commit Lab*/
import org.apache.commons.collections4.list.FixedSizeList;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Array;
import java.util.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.xml.transform.TransformerFactory;

public class CollectionUtilsTest {

  @Test
  void addAll() {
	List<Integer> l = new ArrayList<>();
	List<Integer> r = new ArrayList<>();
	Set<Integer> s = new HashSet<Integer>();
	s.add(1);
	l.add(1);
	testIterator<Integer> t = new testIterator(5);
	testIterator<Integer> v = new testIterator(-1);

	assertFalse(CollectionUtils.addAll(s,1));
	assertFalse(CollectionUtils.addAll(l, r));
    assertTrue(CollectionUtils.addAll(l, 7, 2, 1));
    assertEquals(Arrays.asList(1, 7, 2, 1), l);
    assertTrue(CollectionUtils.addAll(r, t));
    assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5),r);
    assertFalse(CollectionUtils.addAll(r, v));
    assertTrue(CollectionUtils.addAll(r, l));
    assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 1, 7, 2, 1),r);
  }

  @Test
  void addAllEnumeration() {
    List<Integer> l = new ArrayList<>();
    Vector<Integer> v = new Vector<>();
    
    assertFalse(CollectionUtils.addAll(l, v.elements()));
    v.add(3);
    v.add(1);

    assertTrue(CollectionUtils.addAll(l, v.elements()));
    assertEquals(Arrays.asList(3, 1), l);
  }

  @Test
  void addAllIterator() {
    List<Integer> l = new ArrayList<>();
    List<Integer> v = Arrays.asList(1, 5, 2, 1, 0);

    assertTrue(CollectionUtils.addAll(l, v.iterator()));
    assertEquals(Arrays.asList(1, 5, 2, 1, 0), l);
  }

  @Test
  void addIgnoringNull() {
    List<Integer> l = new ArrayList<>();

    assertTrue(CollectionUtils.addIgnoreNull(l, 3));
    assertEquals(List.of(3), l);

    assertFalse(CollectionUtils.addIgnoreNull(l, null));

    assertThrows(NullPointerException.class, () -> CollectionUtils.addIgnoreNull(null, null));
  }

  @Test
  void cardinality() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);

    assertEquals(CollectionUtils.cardinality(5, l), 1);
    assertEquals(CollectionUtils.cardinality(1, l), 2);
    assertEquals(CollectionUtils.cardinality(100, l), 0);

    assertThrows(NullPointerException.class, () -> CollectionUtils.cardinality(5, null));
  }

  @Test
  void collate() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);
    List<Integer> v = Arrays.asList(10, 25, 1, 0, 7);

    assertEquals(Arrays.asList(1, 5, 2, 1, 0, 10, 25, 1, 0, 7), CollectionUtils.collate(l, v));
    assertEquals(Arrays.asList(1, 1, 5, 2, 1, 0, 10, 25, 1, 0, 7, 8), CollectionUtils.collate(Arrays.asList(1), Arrays.asList(1, 5, 2, 1, 0, 10, 25, 1, 0, 7, 8)));
    assertThrows(NullPointerException.class, () -> CollectionUtils.collate(l, null));
    assertThrows(NullPointerException.class, () -> CollectionUtils.collate(null, v));
    assertThrows(NullPointerException.class, () -> CollectionUtils.collate(l, v, null));
    assertEquals(Arrays.asList(1, 5, 2, 1, 0, 10, 25, 1, 0, 7), CollectionUtils.collate(l, v, false));
    assertEquals(Arrays.asList(1, 5, 2, 1, 0, 10, 25, 1, 0, 7), CollectionUtils.collate(l, v, true));
 }

  @Test
  void collect() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);
	testIterator<Integer> testIterator = new testIterator(5);
	testIterator<Integer> nullIterator = null;
    List<Float> f = new ArrayList<Float>();
    List<Float> a = Arrays.asList(0f, 1f, 2f, 3f, 4f, 5f);
    
    Transformer<Integer, Float> t = Integer::floatValue;
    
    assertEquals(a, CollectionUtils.collect(nullIterator, t, a));
    assertEquals(Arrays.asList(1f, 5f, 2f, 1f, 0f), CollectionUtils.collect(l, t));
    assertEquals(a, CollectionUtils.collect(testIterator, t));
    assertEquals(a, CollectionUtils.collect(testIterator.iterator(), t));
    
  }

  @Test
  void containsAll() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);
    List<Integer> v = Arrays.asList(1, 5, 2, 1, 0);
    List<Integer> k = Arrays.asList(1, 5, null, 1, 0);
    Collection<Object> e = CollectionUtils.emptyCollection();
    

    assertTrue(CollectionUtils.containsAll(l, v));
    assertTrue(CollectionUtils.containsAll(l, e));
    assertFalse(CollectionUtils.containsAll(l, k));
    
    List<Integer> v_ = Arrays.asList(1, 50, 2, 1, 0);
    assertFalse(CollectionUtils.containsAll(l, v_));
  }

  @Test
  void containsAny() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);
    List<Integer> v = Arrays.asList(0, 0, 0, 0, 0);
    List<Integer> v_ = Arrays.asList(10, 20, 30, 40, 50);
    List<Integer> a = Arrays.asList(1);
    List<Integer> b = Arrays.asList(1,0);

    assertTrue(CollectionUtils.containsAny(l, v));
    assertFalse(CollectionUtils.containsAny(l, v_));

    assertTrue(CollectionUtils.containsAny(l, 1, 2, 3, 4));
    assertFalse(CollectionUtils.containsAny(l, 50, 20, 3, 4));
    
    assertTrue(CollectionUtils.containsAny(a, b));
    assertTrue(CollectionUtils.containsAny(a, 1,0));
  }

  @Test
  void countMatches() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);

    assertEquals(0, CollectionUtils.<Integer>countMatches(l, value -> value > 10));
    assertEquals(1, CollectionUtils.<Integer>countMatches(l, value -> value > 4));
  }

  @Test
  void disjunction() {
	List<Integer> l = Arrays.asList(1, 2, 3);
	List<Integer> m = Arrays.asList(3, 4, 5);
	List<Integer> dis = Arrays.asList(1, 2 , 4, 5);
	    
	assertTrue(CollectionUtils.isEqualCollection(dis, CollectionUtils.disjunction(l, m)));
	    
  }

  @Test
  void emptyIfNull() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);

    assertEquals(l, l);
    assertEquals(CollectionUtils.emptyCollection(), CollectionUtils.emptyIfNull(null));
  }

  @Test
  void exists() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);

    assertTrue(CollectionUtils.exists(l, value -> value > 1));
    assertTrue(CollectionUtils.exists(l, value -> value >= 5));
    assertFalse(CollectionUtils.exists(l, value -> value > 5));
  }

  @Test
  void extractSingleton() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);

    assertEquals(1, CollectionUtils.extractSingleton(List.of(1)));

    assertThrows(IllegalArgumentException.class, () -> CollectionUtils.extractSingleton(l));
    assertThrows(NullPointerException.class, () -> CollectionUtils.extractSingleton(null));
  }

  @Test
  void filter() {
	   List<Integer> l = Arrays.asList(1,5,2,1,0);
	   List<Integer> m = Arrays.asList(1,3,5,7,9);
	   Predicate<Integer> isOdd = value -> value % 2 == 1;
	   assertFalse(CollectionUtils.filter(l,null));
	   assertFalse(CollectionUtils.filter(null,isOdd));
	   
	   List<Integer> aList = new ArrayList<Integer>();
	   aList.add(1);
	   aList.add(5);
	   aList.add(2);
	   aList.add(1);
	   aList.add(0);
	   assertTrue(CollectionUtils.filter(aList,isOdd));
	   List<Integer> bList = new ArrayList<Integer>();
	   bList.add(1);
	   bList.add(5);
	   bList.add(1);
	   assertEquals(aList,bList);
  }


  @Test
  void filterInverse() {
	List<Integer> l = Arrays.asList(1,5,2,1,0);
  	List<Integer> m = Arrays.asList(1,3,5,7,9);
	Predicate<Integer> isOdd = value -> value % 2 == 0;
	assertFalse(CollectionUtils.filterInverse(l,null));
	assertFalse(CollectionUtils.filterInverse(null,isOdd));

	List<Integer> aList = new ArrayList<Integer>();
	aList.add(1);
	aList.add(5);
	aList.add(2);
	aList.add(1);
	aList.add(0);
	assertTrue(CollectionUtils.filterInverse(aList,isOdd));
	List<Integer> bList = new ArrayList<Integer>();
	bList.add(1);
	bList.add(5);
	bList.add(1);
	assertEquals(aList,bList);
  	
  }

  @Test
  void find() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);

    assertEquals(1, CollectionUtils.find(l, value -> value.equals(1)));
    assertNull(CollectionUtils.find(l, value -> value.equals(10)));
    assertNull(CollectionUtils.find(null, value -> value.equals(10)));
    assertNull(CollectionUtils.find(l, null));
    assertNull(CollectionUtils.find(null, null));
  }

  @Test
  void forAllButLastDo() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);

    assertEquals(0, CollectionUtils.forAllButLastDo(l, val -> val++));
    assertEquals(0, CollectionUtils.forAllButLastDo(l.iterator(), val -> val++));
  }

  @Test
  void forAllDo() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);
    List<Integer> E = new ArrayList <> ();
    List<Integer> F = new ArrayList <> ();
    var t = new Closure() {
            public void execute(Object o) {
              Integer k = (Integer) o;
            }
      	  };
    assertEquals(CollectionUtils.forAllDo(l, t),t);
    assertEquals(CollectionUtils.forAllDo(l.iterator(), t),t);
    CollectionUtils.forAllDo(l, E::add);
    CollectionUtils.forAllDo(l.iterator(), F::add);
    assertEquals(l,E);
    assertEquals(l,F);
  }

  @Test
  void get() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);
    Map<Integer, Integer> M = new HashMap<>();
    Vector<Integer> v = new Vector<>();
    v.add(1);
    v.add(2);
    Object[] objectArray = new Object[] {1};
    

    l.forEach(el -> M.put(l.indexOf(el), el));

    assertEquals(1, CollectionUtils.get(l, 0));
    assertEquals(1, CollectionUtils.get(l.iterator(), 0));
    assertEquals(Map.entry(2, 2), CollectionUtils.get(M, 2));
    assertEquals(1, CollectionUtils.get((Object)l, 0));
    assertEquals(1, CollectionUtils.get((Object)(l.iterator()), 0));
    assertEquals(Map.entry(2, 2), CollectionUtils.get((Object)M, 2));
    assertEquals(1, CollectionUtils.get((Object)(v.elements()), 0));
    assertEquals(1, CollectionUtils.get(objectArray, 0));
    //assertEquals(1, 
    //		CollectionUtils.get(, 0));
    
    
    assertThrows(IndexOutOfBoundsException.class,() -> CollectionUtils.get(l,-1));
    assertThrows(IllegalArgumentException.class,() -> CollectionUtils.get((Object)null,0));
  }

  @Test
  void getCardinalityMap() {
    List<Integer> l = Arrays.asList(1, 5, 2, 1, 0);

    Map<Integer, Integer> count = new HashMap<>();
    count.put(1, 2);
    count.put(5, 1);
    count.put(2, 1);

    assertNotEquals(count, CollectionUtils.getCardinalityMap(l));

    count.put(0, 1);
    assertEquals(count, CollectionUtils.getCardinalityMap(l));
  }

  @Test
  void intersection() {
    List<Integer> L = Arrays.asList(1, 5, 2, 1, 0);
    List<Integer> E = Arrays.asList(1, 5, 2, 1, 0);
    List<Integer> N = Arrays.asList(10, 50, 20, 10, 10);
    List<Integer> S = Arrays.asList(1, 65, 20, 10, 230);

    assertEquals(Arrays.asList(0, 1, 1, 2, 5), CollectionUtils.intersection(L, E));
    assertEquals(List.of(), CollectionUtils.intersection(L, N));
    assertEquals(List.of(1), CollectionUtils.intersection(L, S));
  }

  @Test
  void isEmpty() {
    assertTrue(CollectionUtils.isEmpty(CollectionUtils.emptyCollection()));
    assertFalse(CollectionUtils.isEmpty(Arrays.asList(1, 2, 3)));
  }

  @Test
  void isEqualCollection() {
    List<Integer> L = Arrays.asList(1, 2, 3, 4);
    List<Integer> E = Arrays.asList(1, 2, 3, 4);
    List<Integer> N = Arrays.asList(1, 2, 6, 4);
    List<Integer> O = Arrays.asList(1, 2);
    List<Integer> S = Arrays.asList(1, 2, 6, 6);

    assertTrue(CollectionUtils.isEqualCollection(L, L));
    assertTrue(CollectionUtils.isEqualCollection(L, E));
    assertFalse(CollectionUtils.isEqualCollection(L, N));
    assertFalse(CollectionUtils.isEqualCollection(L, O));
    assertFalse(CollectionUtils.isEqualCollection(L, S));

    Equator<Integer> equator = new Equator<>() {
      @Override
      public boolean equate(Integer o1, Integer o2) {
        return o1.equals(o2);
      }

      @Override
      public int hash(Integer o) {
        return o.hashCode();
      }

    };

    assertThrows(NullPointerException.class, () -> CollectionUtils.isEqualCollection(L, L, null));
    assertTrue(CollectionUtils.isEqualCollection(L, L, equator));
    assertTrue(CollectionUtils.isEqualCollection(L, E, equator));
    assertFalse(CollectionUtils.isEqualCollection(L, N, equator));
    assertFalse(CollectionUtils.isEqualCollection(L, O, equator));
  }

  @Test
  void isFull() {
	  assertThrows(NullPointerException.class, () -> CollectionUtils.isFull( null));
	  assertEquals(false, CollectionUtils.isFull(List.of()));

	  List<Integer> coll = new ArrayList<>(5);
	  FixedSizeList<Integer> L = FixedSizeList.fixedSizeList(coll);

	  assertEquals(true, CollectionUtils.isFull(L));

	  coll.add(1);
	  assertEquals(true, CollectionUtils.isFull(L));
	  }

  @Test
  void isNotEmpty() {
    assertFalse(CollectionUtils.isNotEmpty(CollectionUtils.emptyCollection()));
    assertTrue(CollectionUtils.isNotEmpty(Arrays.asList(1, 2, 3)));
  }

  @Test
  void isProperSubCollection() {
    List<Integer> l = Arrays.asList(1,2,3,3,4,5);
    List<Integer> m = Arrays.asList(3,4,5);
    List<Integer> n = Arrays.asList(1,2,3,4,5,5,5);
    assertTrue(CollectionUtils.isProperSubCollection(m, l));
    assertFalse(CollectionUtils.isProperSubCollection(l, l));
    assertFalse(CollectionUtils.isProperSubCollection(n, l));
  }

  @Test
  void isSubCollection() {
    List<Integer> l = Arrays.asList(1,2,3,3,4,5);
    List<Integer> m = Arrays.asList(3,4,5);
    List<Integer> n = Arrays.asList(1,2,3,4,5,5,5);
    assertTrue(CollectionUtils.isSubCollection(m, l));
    assertTrue(CollectionUtils.isSubCollection(l, l));
    assertFalse(CollectionUtils.isSubCollection(n, l));
  }

  @Test
  void matchesAll() {
    Predicate<Integer> predicate = val -> val % 2 == 0;

    assertTrue(CollectionUtils.matchesAll(Arrays.asList(2, 4), predicate));
    assertFalse(CollectionUtils.matchesAll(Arrays.asList(2, 4, 5), predicate));

    assertFalse(CollectionUtils.matchesAll(Arrays.asList(2, 4, 5), null));
    assertTrue(CollectionUtils.matchesAll(null, predicate));
    assertFalse(CollectionUtils.matchesAll(null, null));
  }

  @Test
  void maxSize() {
    assertThrows(NullPointerException.class, () -> CollectionUtils.maxSize( null));
    assertEquals(-1, CollectionUtils.maxSize(List.of()));

    List<Integer> coll = new ArrayList<>(5);
    FixedSizeList<Integer> L = FixedSizeList.fixedSizeList(coll);

    assertEquals(0, CollectionUtils.maxSize(L));

    coll.add(1);
    assertEquals(1, CollectionUtils.maxSize(L));
  }

  @Test
  void permutations() {

    List<Integer> L = Arrays.asList(1, 2, 3);

    assertThrows(NullPointerException.class, () -> CollectionUtils.permutations( null));

    Collection<List<Integer>> P = CollectionUtils.permutations(L);

    assertEquals(6, P.size());
    assertEquals(Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(1, 3, 2),
            Arrays.asList(3, 1, 2),
            Arrays.asList(3, 2, 1),
            Arrays.asList(2, 3, 1),
            Arrays.asList(2, 1, 3)
    ), P);
  }

  @Test
  void predicatedCollection() {
    Predicate<Integer> predicate = value -> value % 2 == 0;

    assertIterableEquals(Arrays.asList(2, 4), CollectionUtils.predicatedCollection(Arrays.asList(2, 4), predicate));
    assertThrows(IllegalArgumentException.class, () -> CollectionUtils.predicatedCollection(Arrays.asList(1, 1, 3), predicate));
    assertThrows(NullPointerException.class, () -> CollectionUtils.predicatedCollection(null, predicate));
    assertThrows(NullPointerException.class, () -> CollectionUtils.predicatedCollection(null,null));
  }

  @Test
  void removeAll() {
  List L = Arrays.asList(1,2,3,4);
  List R = Arrays.asList(1,2);
  List V = Arrays.asList(5,6);

  assertEquals(Arrays.asList(3,4),CollectionUtils.removeAll(L, R));

  Equator<Integer> equator = new Equator<>() {
      @Override
      public boolean equate(Integer o1, Integer o2) {
        return o1.equals(o2);
      }

      @Override
      public int hash(Integer o) {
        return o.hashCode();
      }

    };
    
assertEquals(Arrays.asList(1,2,3,4), CollectionUtils.removeAll(L, V, equator));
assertEquals(Arrays.asList(3,4), CollectionUtils.removeAll(L, R, equator));
  }
  
  @Test
  void retainAll() {
	  List L = Arrays.asList(1, 2, 3);
	  List R = new ArrayList<>();

	    Equator<Integer> equator = new Equator<>() {
	          @Override
	          public boolean equate(Integer o1, Integer o2) {
	            return o1.equals(o2);
	          }

	          @Override
	          public int hash(Integer o) {
	            return o.hashCode();
	          }

	   };
	   
	    assertEquals(List.of(), CollectionUtils.retainAll(L, R));
	    assertEquals(List.of(), CollectionUtils.retainAll(L, R, equator));
	    R.add(1);
	    assertEquals(List.of(1), CollectionUtils.retainAll(L, R, equator));
	  }



  @Test
  void reverseArray() {
    Integer[] L = {1, 2, 3, 4, 5, 6, 10};
    Integer[] R = {10, 6, 5, 4, 3, 2, 1};

    CollectionUtils.reverseArray(L);
    assertIterableEquals(Arrays.asList(R), Arrays.asList(L));
  }

  @Test
  void select() {
    List<Integer> L = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    List<Integer> A = Arrays.asList(1, 3, 5, 7);
    List<Integer> B = Arrays.asList(2, 4, 6, 8);
    List<Integer> O = new ArrayList<Integer>();
    List<Integer> R = new ArrayList<Integer>();
    testIterator<Integer> t = new testIterator(5);
    
    Predicate<Integer> predicate = val -> val % 2 == 0;
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), L);
    assertEquals(Arrays.asList(2, 4, 6, 8), CollectionUtils.select(L, predicate));

    CollectionUtils.select(null, predicate, A);
    assertEquals(Arrays.asList(1, 3, 5, 7), A);

    CollectionUtils.select(L, null, A);
    assertEquals(Arrays.asList(1, 3, 5, 7), A);

    CollectionUtils.select(null, null, A);
    assertEquals(Arrays.asList(1, 3, 5, 7), A);

    assertThrows(NullPointerException.class, () -> CollectionUtils.select(L, predicate, null));
    
    assertEquals(Arrays.asList(0,2,4), CollectionUtils.select(t, predicate));
    
    assertTrue(CollectionUtils.isEqualCollection(O, CollectionUtils.select(L, predicate, O, R)));
    assertTrue(CollectionUtils.isEqualCollection(O, B) && CollectionUtils.isEqualCollection(R, A));
    
  }
  
  //Private class to test iterable
  private class testIterator<E> implements Iterable<E> {
      	public Integer i = 0;
      	
      	public testIterator(int k) {
      		this.i = k;
      	}
	    // code for data structure
	    public Iterator<E> iterator() {
	        return new CustomIterator<E>(this);
	    }
	}
	private class CustomIterator<T> implements Iterator<T> {
	    public Integer c;
	    public Integer l;
	    // constructor
	    public CustomIterator(@SuppressWarnings("rawtypes") testIterator obj) {
	        c = 0;
	        l = (Integer) obj.i;
	    }
	      
	    // Checks if the next element exists
	    public boolean hasNext() {
	    	return this.c<=this.l;
	    }
	      
	    // moves the cursor/iterator to next element
	    @SuppressWarnings("unchecked")
		public T next() {
	    	Integer o = this.c;
	    	this.c = this.c + 1;
	    	return (T) o;
	    }
	    
	}

  @Test
  void selectRejected() {
    Predicate<? super Integer> dividedByTwo = value -> value % 2 == 0;
    testIterator<Integer> t = new testIterator<Integer>(5);
    
    assertEquals(Arrays.asList(1, 3, 5), CollectionUtils.selectRejected(t, dividedByTwo));
    assertEquals(Arrays.asList(1, 3, 5), CollectionUtils.selectRejected(Arrays.asList(1, 2, 3, 4, 5, 6), dividedByTwo));
    assertTrue(CollectionUtils.selectRejected(Arrays.asList(0, 2, 10, 4, 8, 6), dividedByTwo).isEmpty());

    List<Integer> output = new ArrayList<>();

    CollectionUtils.selectRejected(Arrays.asList(1, 2, 3, 4, 5, 6), dividedByTwo, output);
    assertEquals(Arrays.asList(1, 3, 5), output);

    output.clear();

    CollectionUtils.selectRejected(Arrays.asList(0, 2, 10, 4, 8, 6), dividedByTwo, output);
    assertTrue(output.isEmpty());
    
    
  }

  @Test
  void size() {
    	  assertEquals(CollectionUtils.size(null),0);
	  
	  assertEquals(CollectionUtils.size(Map.of()), 0);
	  
	  assertEquals(CollectionUtils.size(new ArrayList<>(0)),0);
	  assertEquals(CollectionUtils.size(List.of(1)), 1);
	  
	  assertEquals(CollectionUtils.size(new ArrayList<>(0).iterator()),0);
	  
	  Vector<Integer> vetor =  new Vector<>();
	  assertEquals(CollectionUtils.size(vetor.elements()),0);
	  vetor.add(1);
	  vetor.add(2);
	  vetor.add(3);
	  assertEquals(CollectionUtils.size(vetor.elements()),3);
  }

  @Test
  void sizeIsEmpty() {
	Object[] testEmpty =  new Object[]{};
	Object[] testNotEmpty =  new Object[]{1};
	
    assertFalse(CollectionUtils.sizeIsEmpty(List.of(1)));
    assertTrue(CollectionUtils.sizeIsEmpty(new ArrayList<>()));

    assertFalse(CollectionUtils.sizeIsEmpty(Map.of(1, 1, 2, 2, 3, 3, 4, 4, 5, 5)));
    assertTrue(CollectionUtils.sizeIsEmpty(Map.of()));

    assertFalse(CollectionUtils.sizeIsEmpty(new int[]{1, 2, 3, 4}));
    assertTrue(CollectionUtils.sizeIsEmpty(new int[]{}));

    assertFalse(CollectionUtils.sizeIsEmpty(List.of(1).iterator()));
    assertTrue(CollectionUtils.sizeIsEmpty(new ArrayList<>().iterator()));

    Vector<Integer> V = new Vector<>();
    assertTrue(CollectionUtils.sizeIsEmpty(V.elements()));

    V.add(1);
    assertFalse(CollectionUtils.sizeIsEmpty(V.elements()));

    assertThrows(IllegalArgumentException.class, () -> CollectionUtils.sizeIsEmpty(1));
    
    assertFalse(CollectionUtils.sizeIsEmpty(new testIterator(1)));
    assertTrue(CollectionUtils.sizeIsEmpty(new testIterator(-1)));
    
    assertTrue(CollectionUtils.sizeIsEmpty(null));
    
    assertTrue(CollectionUtils.sizeIsEmpty(testEmpty));
    assertFalse(CollectionUtils.sizeIsEmpty(testNotEmpty));
    
  }
  

  @Test
  void subtract() {
    List<Integer> a = Arrays.asList(1,2,3);
	  List<Integer> b = Arrays.asList(0,1,2);
	  assertEquals(CollectionUtils.subtract(a, b), Arrays.asList(3));
  }

  @Test
  void synchronizedCollection() {
    List<Integer> L = Arrays.asList(1, 2, 4, 5);

    Collection<Integer> synchronizedCollection = CollectionUtils.synchronizedCollection(L);
    assertIterableEquals(Arrays.asList(1, 2, 4, 5), synchronizedCollection);
  }

  @Test
  void transform() {
    List<Integer> L = Arrays.asList(1, 2, 4, 5);
    Set<Integer> S = new HashSet<Integer>(L);

    Transformer<Integer, Integer> T = value -> value%2;

    CollectionUtils.transform(L, null);
    assertEquals(Arrays.asList(1, 2, 4, 5), L);

    CollectionUtils.transform(null, T);
    assertEquals(Arrays.asList(1, 2, 4, 5), L);

    CollectionUtils.transform(null, null);
    assertEquals(Arrays.asList(1, 2, 4, 5), L);

    CollectionUtils.transform(L, T);
    assertEquals(Arrays.asList(1, 0, 0, 1), L);
    
    CollectionUtils.transform(S, T);
    assertEquals(Set.of(1, 0), S);
  }

  @Test
  void transformingCollection() {
    List<Integer> l =  Arrays.asList(1,2,3,4);

    Transformer<Integer, Integer> t = value -> value * 2;
    
    
    assertNotEquals(l, CollectionUtils.transformingCollection(l, t));
  }

  @Test
  void union() {
	    List<Integer> l =  Arrays.asList(1,2,3,4);
	    List<Integer> m =  Arrays.asList(5,6,7);
	    assertEquals(CollectionUtils.union(l, m), Arrays.asList(1,2,3,4,5,6,7));
  }

  @Test
  void unmodifiableCollection() {
    List<Integer> l =  Arrays.asList(1,2,3,4);

    assertIterableEquals(l, CollectionUtils.unmodifiableCollection(l));
    assertNotEquals(l, CollectionUtils.unmodifiableCollection(l));
  }
}
