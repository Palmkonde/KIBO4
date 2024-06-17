#include <bits/stdc++.h>
#include "CVec3.h"
using namespace std;
using dd = double;
using td = tuple<tuple<dd,dd,dd>,tuple<dd,dd,dd>>;

const dd INF = 1e9;

class Converter
{
    // CVec3 L1,L2;
    // CVec3 B1,B2;

    // Converter()
    // {
    //     L1 = CVec3(0,0,0);
    //     L2 = CVec3(0,0,0);
    //     B1 = CVec3(0,0,0);
    //     B2 = CVec3(0,0,0);
    // }
public:
    CVec3 t2v(tuple<dd,dd,dd> a)
    {
        float x,y,z;
        tie(x,y,z) = a;
        return CVec3(x,y,z);
    }
};

int inline GetIntersection(float fDst1, float fDst2, CVec3 P1, CVec3 P2, CVec3 &Hit)
{
    if ((fDst1 * fDst2) >= 0.0f)
        return 0;
    if (fDst1 == fDst2)
        return 0;
    Hit = P1 + (P2 - P1) * (-fDst1 / (fDst2 - fDst1));
    return 1;
}

int inline InBox(CVec3 Hit, CVec3 B1, CVec3 B2, const int Axis)
{
    if (Axis == 1 && Hit.z > B1.z && Hit.z < B2.z && Hit.y > B1.y && Hit.y < B2.y)
        return 1;
    if (Axis == 2 && Hit.z > B1.z && Hit.z < B2.z && Hit.x > B1.x && Hit.x < B2.x)
        return 1;
    if (Axis == 3 && Hit.x > B1.x && Hit.x < B2.x && Hit.y > B1.y && Hit.y < B2.y)
        return 1;
    return 0;
}

// returns true if line (L1, L2) intersects with the box (B1, B2)
// returns intersection point in Hit
int CheckLineBox(CVec3 B1, CVec3 B2, CVec3 L1, CVec3 L2, CVec3 &Hit)
{
    if (L2.x < B1.x && L1.x < B1.x)
        return false;
    if (L2.x > B2.x && L1.x > B2.x)
        return false;
    if (L2.y < B1.y && L1.y < B1.y)
        return false;
    if (L2.y > B2.y && L1.y > B2.y)
        return false;
    if (L2.z < B1.z && L1.z < B1.z)
        return false;
    if (L2.z > B2.z && L1.z > B2.z)
        return false;
    if (L1.x > B1.x && L1.x < B2.x &&
        L1.y > B1.y && L1.y < B2.y &&
        L1.z > B1.z && L1.z < B2.z)
    {
        Hit = L1;
        return true;
    }
    if ((GetIntersection(L1.x - B1.x, L2.x - B1.x, L1, L2, Hit) && InBox(Hit, B1, B2, 1)) || (GetIntersection(L1.y - B1.y, L2.y - B1.y, L1, L2, Hit) && InBox(Hit, B1, B2, 2)) || (GetIntersection(L1.z - B1.z, L2.z - B1.z, L1, L2, Hit) && InBox(Hit, B1, B2, 3)) || (GetIntersection(L1.x - B2.x, L2.x - B2.x, L1, L2, Hit) && InBox(Hit, B1, B2, 1)) || (GetIntersection(L1.y - B2.y, L2.y - B2.y, L1, L2, Hit) && InBox(Hit, B1, B2, 2)) || (GetIntersection(L1.z - B2.z, L2.z - B2.z, L1, L2, Hit) && InBox(Hit, B1, B2, 3)))
        return true;

    return false;
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

int main()
{
    tuple<dd,dd,dd> p1 = {10.901, -7.926, 4.811};
    tuple<dd,dd,dd> p2 = {10.934164762417753, -7.932210374629813, 4.796149683854322
};
    tuple<dd,dd,dd> p3 = {10.3, -6.853, 4.945};


    Converter con = Converter();
    // Vec3 data type    
    CVec3 v1,v2,v3,B1,B2,hitPoint,tmpHitPoint;
    v1 = con.t2v(p1);
    v2 = con.t2v(p2);
    v3 = con.t2v(p3);

    td KOZ1[2] = {
        {{10.89, -9.5, 4.27},{11.6, -9.45, 5.00}}, //ขยาย x+0.03 | z+0.03
        {{10.25, -9.5, 4.94},{10.90, -9.45, 5.62}} //ขยาย z-0.03 | x+0.03
    };

    td KOZ3[2] = {
        {{10.76, -7.40, 4.27},{11.6, -7.35, 5.02}},
        {{10.25, -7.40, 4.92},{10.92, -7.35, 5.62}}
    };

    td KIZ = {
        {10.3, -10.2, 4.32},{11.55, -6.0, 5.57}
    };

    for(double z=0.01;!isIntersectingBox(make_tuple(9.867+z,-6.853,4.945),make_tuple(9.867+z,-6.853,4.945),KIZ);)
    {
        cout << 9.867+z << "\n";
        z += 0.01;
    }

    for(int i=0;i<2;i++)
    {
        cout << isIntersectingBox(p2,p3,KOZ3[i]) << " ";
    }
    return 0;
}