package com.yusun.cartracker.helper;

public class FenceCalculate {
	/**
	 * 地球半径
	 */
	private static double EARTH_RADIUS = 6378138.0;
	
	private static double rad(double d)
	{    
		return d * Math.PI / 180.0;
	}
	/**
	 * 计算是否在圆上（单位/千米）
	 * 
	* @Title: GetDistance 
	* @Description: TODO() 
	* @param radius 半径
	* @param lat1  纬度
	* @param lng1  经度
	* @return    
	* @return double   
	* @throws
	 */
	public static boolean isInCircle(double radius,double lat1, double lng1, double lat2, double lng2)
	{    
		double radLat1 = rad(lat1);    
		double radLat2 = rad(lat2);    
		double a = radLat1 - radLat2;    
		double b = rad(lng1) - rad(lng2);    
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +      
				Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));    
		s = s * EARTH_RADIUS;    
		s = Math.round(s * 10000) / 10000;    
		if(s > radius) {//不在圆上
			return false;
		}else {
			return true;
		}
	}
	/**
	 * 是否在矩形区域内
	* @Title: isInArea 
	* @Description: TODO() 
	* @param lat 测试点经度
	* @param lng 测试点纬度
	* @param minLat 纬度范围限制1 
	* @param maxLat 纬度范围限制2 
	* @param minLng 经度限制范围1 
	* @param maxLng 经度范围限制2 
	* @return    
	* @return boolean   
	* @throws
	 */
	public static boolean isInRectangleArea(double lat,double lng,double minLat,
			double maxLat,double minLng,double maxLng){  
        if(isInRange(lat, minLat, maxLat)){//如果在纬度的范围内  
            if(minLng*maxLng>0){
                if(isInRange(lng, minLng, maxLng)){  
                    return true;  
                }else {  
                    return false;  
                }  
            }else {
                if(Math.abs(minLng)+Math.abs(maxLng)<180){
                    if(isInRange(lng, minLng, maxLng)){  
                        return true;  
                    }else {  
                        return false;  
                    }  
                }else{
                    double left = Math.max(minLng, maxLng);
                    double right = Math.min(minLng, maxLng);  
                    if(isInRange(lng, left, 180)||isInRange(lng, right,-180)){  
                        return true;  
                    }else {  
                        return false;  
                    }  
                }  
            }  
        }else{  
            return false;  
        }  
    } 
	
	/**
     * 判断是否在经纬度范围内 
    * @Title: isInRange 
    * @Description: TODO() 
    * @param point
    * @param left
    * @param right
    * @return    
    * @return boolean   
    * @throws
     */
    public static boolean isInRange(double point, double left,double right){  
        if(point>=Math.min(left, right)&&point<=Math.max(left, right)){  
            return true;  
        }else {  
            return false;  
        }
    }
    
    /**
     * 判断点是否在多边形内 
    * @Title: IsPointInPoly 
    * @Description: TODO() 
    * @param point 测试点
    * @param pts 多边形的点 
    * @return    
    * @return boolean
    * @throws
     */
     /*public static boolean isInPolygon(Point2D.Double point, List<Point2D.Double> pts){  
     	
         int N = pts.size();  
         boolean boundOrVertex = true; 
         int intersectCount = 0;//交叉点数量  
         double precision = 2e-10; //浮点类型计算时候与0比较时候的容差  
         Point2D.Double p1, p2;//临近顶点  
         Point2D.Double p = point; //当前点  
           
         p1 = pts.get(0);      
         for(int i = 1; i <= N; ++i){      
             if(p.equals(p1)){  
                 return boundOrVertex;
             }  
               
             p2 = pts.get(i % N);           
             if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){                  
                 p1 = p2;   
                 continue;
             }  
             
             //射线穿过算法
             if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){
                 if(p.y <= Math.max(p1.y, p2.y)){               
                     if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){  
                         return boundOrVertex;  
                     }  
                       
                     if(p1.y == p2.y){                   
                         if(p1.y == p.y){
                             return boundOrVertex;  
                         }else{
                             ++intersectCount;  
                         }   
                     }else{                     
                         double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;                       
                         if(Math.abs(p.y - xinters) < precision){ 
                             return boundOrVertex;  
                         }  
                           
                         if(p.y < xinters){
                             ++intersectCount;  
                         }   
                     }  
                 }  
             }else{            
                 if(p.x == p2.x && p.y <= p2.y){                 
                     Point2D.Double p3 = pts.get((i+1) % N);                 
                     if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){
                         ++intersectCount;  
                     }else{  
                         intersectCount += 2;  
                     }  
                 }  
             }              
             p1 = p2;
         }  
         if(intersectCount % 2 == 0){//偶数在多边形外  
             return false;  
         } else { //奇数在多边形内  
             return true;
         } 
     }*/
}
