import java.util.ArrayList;

public class BPTree implements BTree{
	private Node root;
	int degree;
	
	public BPTree(int degree) {
		this.degree = degree;
		this.root = new LeafNode(degree);
	}
    
    @Override
    public Integer search(int key) {return root.search(key);}
    
    @Override
    public ArrayList<Integer> search(int start, int end) {return root.search(start, end);}
    
    @Override
    public void add(int key) {
        if (root.countKeys() == 2 * degree - 1) {
            // 루트 노드가 꽉 찼으면 새로운 루트 생성
            Node newRoot = new InternalNode(degree);
            newRoot.setChild(0, root);
            newRoot.split(0, root);
            root = newRoot;  // 새로운 루트를 루트로 설정
        }
        root.insert(key);
    }
    
    // 공통 Node 클래스
    abstract class Node {
        protected int[] keys;
        protected int keyCount;
        protected boolean isLeaf;

        public Node(int degree, boolean isLeaf) {
            this.keys = new int[2 * degree - 1];
            this.keyCount = 0;
            this.isLeaf = isLeaf;
        }
        // 검색
        public abstract Integer search(int key);
        // 범위 검색
        public abstract ArrayList<Integer> search(int start, int end);
        // 삽입
        public abstract void insert(int key);
        // 노드 키 개수
        public int countKeys() {return keyCount;}
        // 키 설정
        public void setKey(int index, int key) {keys[index] = key;}
        // 키 가져오기
        public int getKey(int index) {return keys[index];}
        // 자식 노드 설정
        public abstract void setChild(int index, Node child);
        // 자식 노드 가져오기
        public abstract Node getChild(int index);
        // 분할 메서드
        public abstract void split(int index, Node child);
    }
    
 // 내부 노드 클래스
    class InternalNode extends Node {
        private Node[] children;

        public InternalNode(int degree) {
            super(degree, false);  // 내부 노드는 리프가 아님
            children = new Node[2 * degree];  // 자식 노드는 2*degree 개수만큼
        }

        @Override
        public Integer search(int key) {
            Node currentNode = this; // 현재 노드에서 시작
            while (currentNode != null) {
                int i = 0;
                while (i < currentNode.keyCount && currentNode.keys[i] < key) i++; 
                // 키가 일치하면 반환
                if (i < currentNode.keyCount && currentNode.keys[i] == key) return currentNode.keys[i];
                // 자식 노드가 있다면 해당 자식 노드로 이동
                if (i < currentNode.keyCount && currentNode.getChild(i) != null) currentNode = currentNode.getChild(i); // 자식 노드로 내려감
                 else return null; // 자식이 없거나 값이 없으면 종료                
            }
            return null; // 값을 찾지 못한 경우
        }
        
        @Override
        public ArrayList<Integer> search(int start, int end) {
            ArrayList<Integer> result = new ArrayList<>();
            // 자식 노드가 있을 경우, 리프 노드까지 내려가야 함
            if (children[0] != null) {
                // 내부 노드에서 자식 노드를 차례대로 탐색
                for (int i = 0; i < keyCount; i++) {
                    if (start <= keys[i]) {
                        // 자식 노드가 null이 아니면 search를 호출
                        if (children[i] != null) result.addAll(children[i].search(start, end)); // 자식 노드로 내려가기
                    }
                }
                // 마지막 자식 노드도 범위에 맞게 검색
                if (children[keyCount] != null) result.addAll(children[keyCount].search(start, end));
                
            } else {
                // 리프 노드에 도달한 경우
                for (int i = 0; i < keyCount; i++) {
                    if (keys[i] >= start && keys[i] <= end) result.add(keys[i]);  // 범위 내의 값을 결과에 추가                    
                }
            }
            return result;
        }
        
        @Override
        public void insert(int key) {
            int i = keyCount - 1;
            while (i >= 0 && keys[i] > key) {
                keys[i + 1] = keys[i];
                i--;
            }
            keys[i + 1] = key;
            keyCount++;
        }

        @Override
        public void setChild(int index, Node child) {children[index] = child;}

        @Override
        public Node getChild(int index) {return children[index];}

        @Override
        public void split(int index, Node child) {
            InternalNode newNode = new InternalNode(degree);
            // 분할할 자식 노드에서 키와 자식 노드를 나누기
            for (int i = 0; i < degree - 1; i++) newNode.setKey(i, child.getKey(i + degree));
            for (int i = 0; i < degree; i++) newNode.setChild(i, child.getChild(i + degree));
            // 기존 노드에 키와 자식 추가
            for (int i = keyCount - 1; i >= index; i--) setKey(i + 1, getKey(i));            
            setKey(index, child.getKey(degree - 1));
            keyCount++;
            setChild(index + 1, newNode);  // 새로 생성된 자식 노드를 현재 노드에 추가
        }
    }

    // 리프 노드 클래스
    class LeafNode extends Node {
        private LeafNode next;
        public LeafNode(int degree) {
            super(degree, true);  // 리프 노드는 실제 데이터 저장
        }

        @Override
        public Integer search(int key) { 
        	System.out.println("Leaf_Search");
        
            for (int i = 0; i < keyCount; i++) {
                if (keys[i] == key) return keys[i];               
            }
            return null;
        }

        @Override
        public ArrayList<Integer> search(int start, int end) {
            ArrayList<Integer> result = new ArrayList<>();
            LeafNode current = this;
            while (current != null) {
                for (int i = 0; i < current.keyCount; i++) {
                    if (current.keys[i] >= start && current.keys[i] <= end) result.add(current.keys[i]);                    
                }
                current = current.next;
            }
            return result;
        }
       
        @Override
        public void insert(int key) {
            int i = keyCount - 1;
            while (i >= 0 && keys[i] > key) {
                keys[i + 1] = keys[i];
                i--;
            }
            keys[i + 1] = key;
            keyCount++;
        }

        @Override
        public void setChild(int index, Node child) {}

        @Override
        public Node getChild(int index) {return null;}

        @Override
        public void split(int index, Node child) {}

        // 리프 노드 연결
        public void setNext(LeafNode next) {this.next = next;}
    }

}
