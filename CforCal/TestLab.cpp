#include<bits/stdc++.h>
using namespace std;
using dd = double;
using td = tuple<tuple<dd,dd,dd>,tuple<dd,dd,dd>>;

const double INF = 1e9;

dd KIZ_xmin = 10.3;
dd KIZ_ymin = -10.2;
dd KIZ_Zmin = 4.32;

dd KIZ_xmax = 11.55;
dd KIZ_ymax = -6.0;
dd KIZ_zmax = 5.57;

td KOZ[5] = {
    {{10.783,-9.8899,4.8385}, {11.071,-9.6929,5.0665}},
    {{10.8652, -9.0734, 4.3861}, {10.9628, -8.7314, 4.6401}},
    {{10.185, -8.3826, 4.1475}, {11.665, -8.2826, 4.6725}},
    {{10.7955, -8.0635, 5.1055}, {11.3525, -7.7305, 5.1305}},
    {{10.563, -7.1449, 4.6544}, {10.709, -6.8099, 4.8164}}
};

map<int,tuple<dd,dd,dd>> pos = {
    {0,{10.3, -9.808, 4.389}},
    {1,{11.2625, -10.18, 5.3625}},
    {2,{10.5134, -9.08517, 4.32203}},
    {3,{10.6031, -7.71007, 4.32093}},
    {4,{10.307, -6.67397, 5.09531}},
    {5,{11.102, -8.0304, 5.5676}},   
    {6,{11.543, -8.989, 4.8305}},
    {7,{11.369,-8.5518, 4.48}}, //qr code
    {8,{11.143,-6.7607, 4.9654}} // Goal
};

map<int,int> change;

int idx = 9;
dd dist[1105][1105];
int Next[1105][1105];

void pt(tuple<dd,dd,dd> &tmp)
{
    cout << get<0>(tmp) << ", " << get<1>(tmp) << ", " << get<2>(tmp);
}

bool operator<=(const tuple<dd, dd, dd>& a, const tuple<dd, dd, dd>& b)
{
    dd ta1, ta2, ta3;
    dd tb1, tb2, tb3;

    tie(ta1, ta2, ta3) = a;
    tie(tb1, tb2, tb3) = b;

    return (ta1 >= tb1 && ta2 >= tb2 && ta3 >= tb3);
}

bool isAAB(const tuple<dd,dd,dd> &rayStart, const tuple<dd,dd,dd> &rayEnd, const td &rectangle)
{
    tuple<dd,dd,dd> minPoint,maxPoint;
    tie(minPoint,maxPoint) = rectangle;

    dd invDirX = 1.0/(get<0>(rayEnd) - get<0>(rayStart));
    dd invDirY = 1.0/(get<1>(rayEnd) - get<1>(rayStart));
    dd invDirZ = 1.0/(get<2>(rayEnd) - get<2>(rayStart));

    dd tx1 = (get<0>(minPoint) - get<0>(rayStart)) * invDirX;   
    dd tx2 = (get<0>(maxPoint) - get<0>(rayStart)) * invDirX;

    dd ty1 = (get<1>(minPoint) - get<1>(rayStart)) * invDirY;   
    dd ty2 = (get<1>(maxPoint) - get<1>(rayStart)) * invDirY;

    dd tz1 = (get<2>(minPoint) - get<2>(rayStart)) * invDirZ;
    dd tz2 = (get<2>(maxPoint) - get<2>(rayStart)) * invDirZ;

    dd tmax = max({min(tx1,tx2), min(ty1,ty2), min(tz1,tz2)});
    dd tmin = min({max(tx1,tx2), max(ty1,ty2), max(tz1,tz2)});

    if (tmax < 0 || tmin > tmax) {
        return false;
    }

    return true;
}

double cal(int i,int j)
{
    dd ix,iy,iz;
    dd jx,jy,jz;

    tie(ix,iy,iz) = pos[i];
    tie(jx,jy,jz) = pos[j];

    dd tmp = sqrt(pow(abs(jx - ix), 2) + pow(abs(jy - iy), 2) + pow(jz - iz, 2));

    return tmp;
}

bool isIntersectingBox(const tuple<dd, dd, dd>& p1, const tuple<dd, dd, dd>& p2, const td& rectangle)
{
    tuple<dd, dd, dd> minPoint, maxPoint;
    tie(minPoint, maxPoint) = rectangle;

    dd x1 = get<0>(p1), y1 = get<1>(p1), z1 = get<2>(p1);
    dd x2 = get<0>(p2), y2 = get<1>(p2), z2 = get<2>(p2);

    // Check if the line segment intersects the box in any dimension
    bool intersectX = (min(x1, x2) <= get<0>(maxPoint) && max(x1, x2) >= get<0>(minPoint));
    bool intersectY = (min(y1, y2) <= get<1>(maxPoint) && max(y1, y2) >= get<1>(minPoint));
    bool intersectZ = (min(z1, z2) <= get<2>(maxPoint) && max(z1, z2) >= get<2>(minPoint));

    // If the line segment intersects the box in all three dimensions, it intersects the box
    return intersectX && intersectY && intersectZ;
}

vector<int> printPath(int u,int v)
{
    vector<int> ans;
    if(Next[u][v] == -1) return {};

    ans.push_back(u);
    while(u != v)
    {
        u = Next[u][v];
        ans.push_back(u);
    }
    return ans;
}

int main()
{
    for (dd x = KIZ_xmin; x < KIZ_xmax; x += 0.25)
    {
        for (dd y = KIZ_ymin; y < KIZ_ymax; y += 0.25)
        {
            for (dd z = KIZ_Zmin; z < KIZ_zmax; z += 0.25)
            {
                tuple<dd, dd, dd> now = make_tuple(x, y, z);
                bool insideBox = false;

                for (int i = 0; i < 5; i++)
                {
                    tuple<dd, dd, dd> KOZMIN = get<0>(KOZ[i]);
                    tuple<dd, dd, dd> KOZMAX = get<1>(KOZ[i]);

                    if (get<0>(KOZMIN) <= x && x <= get<0>(KOZMAX) &&
                        get<1>(KOZMIN) <= y && y <= get<1>(KOZMAX) &&
                        get<2>(KOZMIN) <= z && z <= get<2>(KOZMAX))
                    {
                        insideBox = true;
                        break;
                    }
                }

                if (!insideBox)
                {
                    pos[idx++] = now;
                }
            }
        }
    }

    // cout << idx << "<-\n";
    
    for (int i = 0; i < idx; i++)
    {
        for (int j = 0; j < idx; j++)
        {
            if (i == j)
            {
                Next[i][j] = j;
                continue;
            }

            bool intersectBox = false;
            for (int k = 0; k < 5; k++)
            {
                if (isIntersectingBox(pos[i], pos[j], KOZ[k]))
                {
                    intersectBox = true;
                    break;
                }
            }

            if (intersectBox)
            {
                dist[i][j] = INF;
                Next[i][j] = -1;
            }
            else
            {
                dist[i][j] = cal(i, j);
                Next[i][j] = j;
            }
        }
    }

    for(int k=0;k<idx;k++)
    {
        for(int i=0;i<idx;i++)
        {
            for(int j=0;j<idx;j++)
            {
                if(dist[i][k] == INF or dist[k][j] == INF)continue;
                
                if(dist[i][j] > dist[i][k] + dist[k][j])
                {
                    dist[i][j] = dist[i][k] + dist[k][j];
                    Next[i][j] = Next[i][k];
                } 
            }
        }
    }


    // set<int> allPoint;
    // for(int i=0;i<=8;i++)
    // {
    //     for(int j=0;j<=8;j++)
    //     {
    //         if(i==j)continue;
            
    //         vector<int> path = printPath(i,j);
            
    //         for(auto e:path)
    //         {
    //             cout << e << " ";
    //             allPoint.insert(e);
    //         } 
    //         cout << "\n";
    //     }
    // }

    //freopen("output.txt", "w", stdout);
    
    // for(auto e:printPath(0,6)) cout << e << " ";cout <<"\n"; 
    // pt(pos[0]);cout << "\n";
    // pt(pos[371]);cout << "\n";
    // pt(pos[7]);cout << "\n";
    // pt(pos[386]);cout << "\n";
    // pt(pos[8]);cout << "\n";
    // pt(pos[136]);cout << "\n";
    // pt(pos[3]);cout << "\n";
    // pt(pos[150]);cout << "\n";
    // pt(pos[4]);cout << "\n";
    // pt(pos[238]);cout << "\n";
    // pt(pos[5]);cout << "\n";
    // pt(pos[388]);cout << "\n";
    // pt(pos[6]);cout << "\n";
    // pt(pos[8]);cout << "\n";
    
    // tuple<dd, dd, dd> p1 = make_tuple(10.731, -9.366, 4.308);
    // tuple<dd, dd, dd> p2 = make_tuple(11.38,-8.56,4.36); 
    // td KIZ = {{KIZ_xmin,KIZ_ymin,KIZ_Zmin}, {KIZ_xmax,KIZ_ymax,{KIZ_zmax}}};

    int intersectingRectangleIndex = -1;
    for (int i = 0; i < 5; i++)
    {
        if (isIntersectingBox(pos[0],pos[6], KOZ[i]))
        {
            intersectingRectangleIndex = i;
            break;
        }
    }

    // for(dd z=3.76203;z<=10;z+=0.1)
    // {
    //     p1 = make_tuple(10, -7, z);
    //     if(isIntersectingBox(p1,p1,KIZ))
    //     {
    //         cout << "YES";
    //         pt(p1);
    //         break;
    //     }
    // }

    if (intersectingRectangleIndex != -1)
    {
        tuple<dd, dd, dd> minPoint = get<0>(KOZ[intersectingRectangleIndex]);
        tuple<dd, dd, dd> maxPoint = get<1>(KOZ[intersectingRectangleIndex]);

        cout << "Intersecting rectangle: ";
        pt(minPoint);
        cout << " ";
        pt(maxPoint);
        cout << "\n";
    }
    else
    {
        cout << "No intersection with any rectangle.\n";
    }

    return 0;
}