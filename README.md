## Android 서비스 AIDL 이용 예제 (server & client)

### Client에서 server 함수 호출
- getRandomNumber(): 버튼 누를 때마다 호출함
- registerCallback(): 서비스 bind 되면 호출하여 콜백 함수 등록함
- unregisterCallback(): 앱 종료시 호출하여 등록된 콜백 함수 제거함

### Server에서 client 함수 호출
- 콜백이 등록되면 3초마다 등록한 콜백 함수 호출함

### 빌드
Android Studio에서 빌드하거나, AOSP에서는 아래 예와 같이 빌드할 수 있다.
```sh
$ m MyServiceServer
$ m MyServiceClient
``` 
빌드 결과로 MyServiceServer.apk, MyServiceClient.apk 파일이 생성된다.

### 실행
Client 앱만 실행하면 자동으로 server의 service가 bind 된다. 아래와 같이 커맨드로도 실행시킬 수도 있다.
```sh
$ am start -n com.my.myserviceclient/.MainActivity
```

### Logcat 실행 결과 예
```log
15:20:27.541  3995  3995 I MyServiceClient: onCreate()
15:20:27.593  3995  3995 I MyServiceClient: onStart()
15:20:27.593  3995  3995 I MyServiceClient: bindToMyRemoteService()
15:20:27.737  4015  4015 I MyServiceServer: onBind()
15:20:27.739  3995  3995 I MyServiceClient: onServiceConnected() Service is bounded
15:20:27.740  4015  4035 D MyServiceServer: registerCallback: true
15:20:27.744  4015  4015 I MyServiceServer: Call callback function with state: 1
15:20:27.744  4015  4015 D MyServiceServer: callRegisteredCallback() state:1
15:20:27.744  4015  4015 I MyServiceServer: Call onMyServiceStateChanged() with state: 1
15:20:27.744  3995  4007 I MyServiceClient: onMyServiceStateChanged() state: 1
15:20:30.532  4015  4035 I MyServiceServer: getRandomNumber() return randomNum: 583
15:20:30.532  3995  3995 I MyServiceClient: Got randomNum: 583
15:20:30.752  4015  4015 I MyServiceServer: Call callback function with state: 2
15:20:30.752  4015  4015 D MyServiceServer: callRegisteredCallback() state:2
15:20:30.752  4015  4015 I MyServiceServer: Call onMyServiceStateChanged() with state: 2
15:20:30.752  3995  4034 I MyServiceClient: onMyServiceStateChanged() state: 2
15:20:32.333  4015  4035 I MyServiceServer: getRandomNumber() return randomNum: 497
15:20:32.333  3995  3995 I MyServiceClient: Got randomNum: 497
15:20:33.756  4015  4015 I MyServiceServer: Call callback function with state: 3
15:20:33.756  4015  4015 D MyServiceServer: callRegisteredCallback() state:3
15:20:33.756  4015  4015 I MyServiceServer: Call onMyServiceStateChanged() with state: 3
15:20:33.756  3995  4136 I MyServiceClient: onMyServiceStateChanged() state: 3
15:20:35.632  4015  4035 I MyServiceServer: getRandomNumber() return randomNum: 424
15:20:35.632  3995  3995 I MyServiceClient: Got randomNum: 424
15:20:36.760  4015  4015 I MyServiceServer: Call callback function with state: 4
15:20:36.760  4015  4015 D MyServiceServer: callRegisteredCallback() state:4
15:20:36.760  4015  4015 I MyServiceServer: Call onMyServiceStateChanged() with state: 4
15:20:36.760  3995  4008 I MyServiceClient: onMyServiceStateChanged() state: 4
15:20:39.764  4015  4015 I MyServiceServer: Call callback function with state: 5
15:20:39.764  4015  4015 D MyServiceServer: callRegisteredCallback() state:5
15:20:39.764  4015  4015 I MyServiceServer: Call onMyServiceStateChanged() with state: 5
15:20:39.764  3995  4008 I MyServiceClient: onMyServiceStateChanged() state: 5
15:20:41.286  3995  3995 I MyServiceClient: onStop() Unbind service
15:20:41.287  4015  4027 D MyServiceServer: unregisterCallback: true
```