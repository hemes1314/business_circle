package com.ishansong.tools.businessCircle.utils;

import com.ishansong.tools.businessCircle.model.LatLng;

import java.util.List;

/**
 * 点是否在多边形区域内工具类（射线法）
 * @author wubin
 * @date 2017/10/2
 **/
public class SpatialRelationUtil {

    private SpatialRelationUtil() {
    }

    /**
     * 返回一个点是否在一个多边形区域内
     *
     * @param mPoints 多边形坐标点列表
     * @param point   待判断点
     * @return true 多边形包含这个点,false 多边形未包含这个点。
     */
    public static boolean isPolygonContainsPoint(List<LatLng> mPoints, LatLng point) {
        int nCross = 0;
        for (int i = 0; i < mPoints.size(); i++) {
            LatLng p1 = mPoints.get(i);
            LatLng p2 = mPoints.get((i + 1) % mPoints.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数
            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.getLng() == p2.getLng())
                continue;
            // point 在p1p2 底部 --> 无交点
            if (point.getLng() < Math.min(p1.getLng(), p2.getLng()))
                continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.getLng() >= Math.max(p1.getLng(), p2.getLng()))
                continue;
            // 求解 point点水平线与当前p1p2边的交点的 X 坐标
            double x = (point.getLng() - p1.getLng()) * (p2.getLat() - p1.getLat()) / (p2.getLng() - p1.getLng()) + p1.getLat();
            if (x > point.getLat()) // 当x=point.x时,说明point在p1p2线段上
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    /**
     * 返回一个点是否在一个多边形边界上
     *
     * @param mPoints 多边形坐标点列表
     * @param point   待判断点
     * @return true 点在多边形边上,false 点不在多边形边上。
     */
    public static boolean isPointInPolygonBoundary(List<LatLng> mPoints, LatLng point) {
        for (int i = 0; i < mPoints.size(); i++) {
            LatLng p1 = mPoints.get(i);
            LatLng p2 = mPoints.get((i + 1) % mPoints.size());
            // 取多边形任意一个边,做点point的水平延长线,求解与当前边的交点个数

            // point 在p1p2 底部 --> 无交点
            if (point.getLng() < Math.min(p1.getLng(), p2.getLng()))
                continue;
            // point 在p1p2 顶部 --> 无交点
            if (point.getLng() > Math.max(p1.getLng(), p2.getLng()))
                continue;

            // p1p2是水平线段,要么没有交点,要么有无限个交点
            if (p1.getLng() == p2.getLng()) {
                double minX = Math.min(p1.getLat(), p2.getLat());
                double maxX = Math.max(p1.getLat(), p2.getLat());
                // point在水平线段p1p2上,直接return true
                if ((point.getLng() == p1.getLng()) && (point.getLat() >= minX && point.getLat() <= maxX)) {
                    return true;
                }
            } else { // 求解交点
                double x = (point.getLng() - p1.getLng()) * (p2.getLat() - p1.getLat()) / (p2.getLng() - p1.getLng()) + p1.getLat();
                if (x == point.getLat()) // 当x=point.x时,说明point在p1p2线段上
                    return true;
            }
        }
        return false;
    }

}