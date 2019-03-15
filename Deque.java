import tester.Tester;

abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  void updateNext(ANode<T> target) {
    this.next = target;
  }

  void updatePrevious(ANode<T> target) {
    this.prev = target;
  }

  abstract int addNode();
}

class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  int sizeSent() {
    return this.next.addNode();
  }

  int addNode() {
    return 0;
  }
}

class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.next = null;
    this.prev = null;
    this.data = data;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("The given node cannot be null");
    }
    this.next = next;
    this.prev = prev;
    this.data = data;
    this.prev.updateNext(this); // Have to use field of field here
    this.next.updatePrevious(this); // Moving these to a separate method
  }

  int addNode() {
    return 1 + this.next.addNode();
  }
}

class Deque<T> {
  Sentinel<T> header;

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  Deque() {
    this.header = new Sentinel<T>();
  }

  int size() {
    return this.header.sizeSent();
  }
}

class ExamplesDeque {
  Sentinel<String> empty;
  Deque<String> deque1;
  Sentinel<String> alpha;
  Node<String> n1;
  Node<String> n2;
  Node<String> n3;
  Node<String> n4;
  Deque<String> deque2;
  Sentinel<String> bois;
  Node<String> n5;
  Node<String> n6;
  Node<String> n7;
  Node<String> n8;
  Deque<String> deque3;

  void initData() {
    empty = new Sentinel<String>();
    deque1 = new Deque<String>(empty);

    alpha = new Sentinel<String>();
    n1 = new Node<String>("abc", alpha, alpha);
    n2 = new Node<String>("bcd", alpha, n1);
    n3 = new Node<String>("cde", alpha, n2);
    n4 = new Node<String>("def", alpha, n3);
    deque2 = new Deque<String>(alpha);

    bois = new Sentinel<String>();
    n5 = new Node<String>("sauharda", bois, bois);
    n6 = new Node<String>("daniel", bois, n5);
    n7 = new Node<String>("preston", bois, n6);
    n8 = new Node<String>("ethan", bois, n7);
    deque3 = new Deque<String>(bois);
  }

  // remember to test helpers.
  void testSize(Tester t) {
    initData();
    t.checkExpect(this.deque1.size(), 0);
    t.checkExpect(this.deque2.size(), 4);
    t.checkExpect(this.deque3.size(), 4);
  }
}
