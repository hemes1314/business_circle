package com.ishansong.tools.businessCircle;

import com.ishansong.tools.businessCircle.model.LatLng;
import com.ishansong.tools.businessCircle.utils.SpatialRelationUtil;

import java.util.ArrayList;
import java.util.List;

public class PointInPolygonTest {

    public static void main(String[] args) {

        double [][] pointsArr = {
                {104.066671,30.656262},
                {104.07066,30.657474},
                {104.076912,30.657505},
                {104.075044,30.653435},
                {104.074361,30.651152},
                {104.072654,30.652503},
                {104.073391,30.655237},
                {104.073103,30.655905},
                {104.069295,30.65533},
                {104.066654,30.656216},
                {104.066941,30.657085},
                {104.067282,30.658157},
                {104.066137,30.65825},
                {104.066137,30.657567},
                {104.066119,30.65662},
                {104.066299,30.653901},
                {104.066586,30.652239},
                {104.068185,30.651773},
                {104.070629,30.651587},
                {104.071419,30.65342},
                {104.067323,30.654554},
                {104.067053,30.655206},
                {104.066694,30.656169}
        };
        List<LatLng> mPoints = new ArrayList<LatLng>();
        for(double[] ptArr : pointsArr) {
            LatLng point = new LatLng(ptArr[0], ptArr[1]);
            mPoints.add(point);
        }

        LatLng point = new LatLng(104.070723,30.657443);
        boolean flag = SpatialRelationUtil.isPolygonContainsPoint(mPoints, point);
        System.out.println("是否在商圈中："+flag);
    }
}
