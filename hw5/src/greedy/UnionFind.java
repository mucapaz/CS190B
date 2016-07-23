package greedy;

public class UnionFind {

	int dad[];
	
	UnionFind(int size){
		dad = new int[size];
		for(int x=0;x<size;x++){
			dad[x] = x;
		}
	}

	int findSet(int i){
		return (dad[i] == i) ? i : (dad[i] = findSet(dad[i]));
	}

	boolean isSameSet(int i, int j){
		return findSet(i) == findSet(j);
	}

	void unionSet(int i, int j){
		if(!isSameSet(i,j)){
			int x = findSet(i);
			int y = findSet(j);
			dad[y] = dad[x];
		}
	}

}
