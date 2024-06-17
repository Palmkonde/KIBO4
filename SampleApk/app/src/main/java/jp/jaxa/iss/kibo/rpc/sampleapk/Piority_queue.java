package jp.jaxa.iss.kibo.rpc.sampleapk;

class Pair
{
    double dist;
    int node;

    public Pair()
    {
        this.dist = 0;
        this.node = -1;
    }

    public Pair(double first, int second)
    {
        this.dist = first;
        this.node = second;
    }

    public Pair value(){return new Pair(dist,node);}

    @Override
    public String toString()
    {
        return this.dist + "," + this.node;
    }
}

public class Piority_queue
{
    Pair[] Tree = new Pair[1005];
    int Tsize = 0;

    public void push(double dist, int u)
    {
        Tree[Tsize++] = new Pair(dist,u);

        int node = Tsize-1;
        Pair tmp = Tree[node];

        while(node > 0)
        {
            int p = (node-1)/2;

            if(tmp.dist >= Tree[p].dist) break;

            Tree[node] = Tree[p];
            node = p;
        }
        Tree[node] = tmp;
    }

    public void pop()
    {
        int node = 0,c;
        Tree[node] = Tree[--Tsize];

        Pair tmp = Tree[node];

        while((c = 2*node+1) < Tsize)
        {
            if(c + 1 < Tsize && (Tree[c].dist < Tree[c+1].dist)) c++;

            if(Tree[c].dist >= tmp.dist) break;

            Tree[node] = Tree[c];
            node = c;
        }
        Tree[node] = tmp;
    }

    public int top() { return Tree[0].node; }

    public boolean empty()
    {
        return (Tsize == 0);
    }
}
