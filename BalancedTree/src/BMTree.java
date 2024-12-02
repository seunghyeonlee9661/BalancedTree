import java.util.ArrayList;

public class BMTree implements BTree {
	private Node root;
	int degree;
	
	public BMTree(int degree) {
		this.root = null;
		this.degree = degree;
	}
    
    // 특정 값을 찾는 메서드
    @Override
    public Integer search(int key) {
        if (root != null) {
            return root.search(key);
        }
        return null;
    }
    
    // 범위 검색
    @Override
    public ArrayList<Integer> search(int start, int end) {
        ArrayList<Integer> result = new ArrayList<>();
        if (root != null) {
            root.search(start, end, result);
        }
        return result;
    }
    
    // 값 추가
	public void add(int key) {
        if (root == null) {
            root = new Node(degree, true);
            root.setKey(0, key);
        } else {
            if (root.countKeys() == 2 * degree - 1) {
            	Node node = new Node(degree, false);
            	node.setChild(0,root);
            	node.split(0,root);

                int i = 0;
                if (node.getKey(0) < key) {
                    i++;
                }
                node.getChild(i).insert(key);
                root = node;
            } else {
                root.insert(key);
            }
        }
    }

	
	class Node{
		private int degree; // 최소 차수
		private Node[] children; // 자식 노드 배열
		private int[] keys; // 키 배열
		private boolean isLeaf; // 리프 노드 여부
		
		public Node(int degree, boolean isLeaf) {
	        this.degree = degree;
	        this.isLeaf = isLeaf;
	        this.keys = new int[2 * degree - 1];
	        this.children = new Node[2 * degree];
	    }	
		
	    // 키 검색 기능
	    public Integer search(int key) {
	        int i = 0;
	        while (i < countKeys() && keys[i] < key) i++;	        
	        // 검색 결과 반환
	        if (i < countKeys() && keys[i] == key) return keys[i]; 	        
	        // 리프 노드에 도달했으므로 값이 없음
	        if (isLeaf) return null; 	        
	        // 자식 노드로 내려감
	        return children[i].search(key); 
	    }
	    
	    public void search(int start, int end, ArrayList<Integer> result) {
	        int i = 0;
	        while (i < countKeys() && keys[i] < start) i++;  
	        while (i < countKeys() && keys[i] <= end) {
	            if (!isLeaf) children[i].search(start, end, result);            
	            result.add(keys[i]);
	            i++;
	        }
	        if (!isLeaf) children[i].search(start, end, result);	        
	    }
		
	   public void insert(int key) {
	        int i = countKeys() - 1;  // 마지막 키 인덱스        
	        if (isLeaf) {
	            // 리프 노드에 삽입할 위치 찾기
	            while (i >= 0 && keys[i] > key) {
	                keys[i + 1] = keys[i];  // 오른쪽으로 키를 밀기
	                i--;
	            }
	            keys[i + 1] = key;  // 키 삽입
	        } else { // 자식 노드로 내려가서 삽입
	            while (i >= 0 && keys[i] > key) i--;	            
	            i++;  // 자식 노드 인덱스	            
	            // 자식 노드가 가득 차 있으면 분리
	            if (children[i].countKeys() == 2 * degree - 1) {
	                split(i, children[i]);  // 자식 노드 분리
	                if (keys[i] < key) i++;  // 분리 후, 키에 맞는 자식으로 이동	                
	            }	            
	            // 적합한 자식 노드로 내려가서 삽입
	            children[i].insert(key);
	        }
	    }
	    
	    public void split(int i, Node y) {
	        int t = degree;  // 최소 차수	        
	        // 새로운 노드 z 생성
	        Node z = new Node(degree, y.isLeaf());	        
	        // y의 중간 키를 부모 노드로 이동
	        for (int j = 0; j < t - 1; j++) {
	            z.setKey(j, y.getKey(j + t));
	            y.setKey(j + t, 0);  // 원래 노드에서 키 제거
	        }	        
	        // 자식 노드가 있다면 분리된 자식 노드를 z로 이동
	        if (!y.isLeaf()) {
	            for (int j = 0; j < t; j++) {
	                z.setChild(j, y.getChild(j + t));
	                y.setChild(j + t, null);  // 원래 노드에서 자식 제거
	            }
	        }
	        // 부모 노드에 새로운 자식과 키 삽입
	        for (int j = countKeys(); j >= i + 1; j--) setChild(j + 1, getChild(j));	        
	        setChild(i + 1, z);  // z 자식 추가	        
	        for (int j = countKeys() - 1; j >= i; j--) setKey(j + 1, getKey(j));	        
	        setKey(i, y.getKey(t - 1));  // 부모에 중간 키 삽입
	        y.setKey(t - 1, 0);  // y에서 중간 키 제거
	    }
	    
		public boolean isLeaf() {return this.isLeaf;}
		
		public Node getChild(int i) {return this.children[i];}
		
		public void setChild(int i,Node node) {this.children[i] = node;}
		
		public int getKey(int i) {return this.keys[i];}
		
		public void setKey(int i,int key) {this.keys[i] = key;}
		
	    public int countKeys() {
	        int count = 0;
	        for (int key : keys) { if (key == 0) break; count++; }
	        return count;
	    }
		 
	}
}

