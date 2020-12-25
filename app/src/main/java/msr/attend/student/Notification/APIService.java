package msr.attend.student.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAgpN9z4I:APA91bGMoj0TL5bSdw1UJ0HZVsFcfwqY8dzOcl5r_ZbBjZLmEjMMnwaDYQhjTF5bVmwxHhBhPKurju2qP30qmpMN8lZZ6mZE2DYXxrGLQDXzcBRqd_K6fNIpJDUVyLden2D19ML8oZSe"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

