import java.util.HashSet;
import java.util.Random;

public class Main {
	
//	public static void main(String[] args) {
//		// 각 트리의 객체를 생성합니다.
//		final int degree = 2;  // 예시로 degree를 3으로 설정
//		final int n = 10;  // 테스트할 데이터의 크기
//		final int rangeStart = 2;  // 범위 검색 시작 값
//		final int rangeEnd = 5;    // 범위 검색 종료 값
//        long startTime;
//        long endTime;
//		
//        // 랜덤 값 생성을 위한 세트와 랜덤 객체
//        HashSet<Integer> set = new HashSet<>();  // 중복을 방지할 set
//        Random rand = new Random();
//    	// 데이터 삽입 (랜덤 데이터, 중복 방지)
//        while (set.size() < n+1) {  // 중복을 방지하면서 데이터를 삽입
//            int value = rand.nextInt(n+1);
//            set.add(value);  // 중복된 값이 없으면 set에 추가
//        }
//                
//        // B-Tree 삽입 시간
//        startTime = System.nanoTime();
//        BTree bTree = new BTree(degree);  // B-Tree 객체 생성
//        for (int value : set) {
//            bTree.insert(value);  // 중복된 값이 아니면 추가
//        }
//        endTime = System.nanoTime();
//        System.out.println("B-Tree Insertion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
//        bTree.printTree();
//        bTree.printTotalKeys();
//        // 수정 후 트리 상태 출력
//        
//        startTime = System.nanoTime();
//        for (int i = 0; i < n; i++) {          // 새로운 키 (예: 1씩 증가)
//            bTree.search(i);
//        }
//        endTime = System.nanoTime();
//        System.out.println("B-Tree Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");
//
//        // B-Tree 범위 검색 시간
//        startTime = System.nanoTime();
//        bTree.search(rangeStart, rangeEnd);  // 범위 검색
//        endTime = System.nanoTime();
//        System.out.println("B-Tree Range Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");
//        
////        // B-Tree 수정 시간
////        startTime = System.nanoTime();
////        for (int i = 0; i < n; i++) {
////            int oldKey = i;               // 기존 키
////            int newKey = i + 1;           // 새로운 키 (예: 1씩 증가)
////            bTree.update(oldKey, newKey);
////        }
////        endTime = System.nanoTime();
////        System.out.println("B-Tree Update Time: " + convertToMilliseconds(endTime - startTime) + " ms");
//        
//        // B-Tree 삭제 시간
//        startTime = System.nanoTime();
//        for (int i = 0; i < n; i++) {
//            System.out.println("_________삭제값 :" + (i+1) + "________");  
//            bTree.delete(i + 1);       // 삭제 (수정 후 키로 삭제)
//            bTree.printTree();
//        }
//	      endTime = System.nanoTime();
//	      System.out.println("B-Tree Deletion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
//	      bTree.printTotalKeys();
//        
////        startTime = System.nanoTime();
////        System.out.println("삭제값 :" + 6);     
////        bTree.delete(6);  
////        bTree.printTree();
////        endTime = System.nanoTime();
////        System.out.println("B-Tree Deletion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
////        bTree.printTotalKeys();
//    }    
//	
	public static void main(String[] args) {
		final int DEGREE = 2; // 차수
		final int KEYS = 10; // 데이터 개수
		long startTime, endTime; // 시작 시간, 종료 시간
  
		HashSet<Integer> set = randomHash(KEYS); // 데이터 수 만큼 생성
		BTree btree = new BTree(DEGREE); // B-트리 생성
		    
	    // B-Tree 삽입
	    startTime = System.nanoTime(); // 시작
	    for (int value : set) {
	    	btree.insert(value); // 중복된 값이 아니면 추가
		    btree.show();
		    System.out.println();
	    }
	    endTime = System.nanoTime(); // 종료
	    System.out.println("B-Tree Insertion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
	    btree.show();
	    System.out.println();
	    
	    // B-Tree 검색
	    startTime = System.nanoTime();
	    for (int i = 0; i < KEYS; i++) btree.search(i);
	    endTime = System.nanoTime();
	    System.out.println("B-Tree Search Time: " + convertToMilliseconds(endTime - startTime) + " ms");
	    System.out.println();
	    
	    // B-Tree 삭제
	    startTime = System.nanoTime(); // 시작
	    for (int value : set) {
	    	btree.remove(value); // 중복된 값이 아니면 추가
		    btree.show();
		    System.out.println();
	    }
	    endTime = System.nanoTime(); // 종료
	    System.out.println("B-Tree Deletion Time: " + convertToMilliseconds(endTime - startTime) + " ms");
	    btree.show();
	    System.out.println();
	}
	
    // 시간 변환을 위한 메서드
    private static double convertToMilliseconds(long nanoseconds) {
        return nanoseconds / 1000000.0;
    }
    
    private static HashSet<Integer> randomHash(int n) {
    	HashSet<Integer> set = new HashSet<>();    	
    	Random rand = new Random();
    	// 데이터 삽입 (랜덤 데이터, 중복 방지)
        while (set.size() < n) {  // 중복을 방지하면서 데이터를 삽입
            int value = rand.nextInt(n)+1;
            set.add(value);  // 중복된 값이 없으면 set에 추가
        }
		return set;
    }
}
