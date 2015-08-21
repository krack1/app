NotificationListenerService는 API18(4.3)이상 부터 지원 하므로 이를 명심한다.  

NOtificationListenerService를 상속 받는다.  

public class SimpleKitkatNotificationListener extends NotificationListenerService {  

notification에 새로운 메세지가 뜰때마다 동작한다.  
@Override  
public void onNotificationPosted(StatusBarNotification sbn) {  
//..............  
}  

notification에 있는 메세지들을 삭제한다.  
@Override  
public void onNotificationRemoved(StatusBarNotification sbn) {  
//..............  
}  
}  

manifests에 밑에 코드를 등록해야지 사용 할 수 있다.  
 <service  
            android:name=".NotificationReceive"  
            android:label="@string/service_name"  
            android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE" >  
            <intent-filter>  
                <action android:name="android.service.notification.NotificationListenerService" />  
            </intent-filter>  
</service>  

마지막으로 notification은 외부에서 오는 메세지이기 때문에 보안이 걸려있다.  
이를 풀려면 설정 -> 보안 -> notificaiton access 에서 해당 어플을 허가해준다.  
  
Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");  
                startActivity(intent);  
위의 인텐트 명령어를 통해 notification access로 넘어갈 수도 있다.  


