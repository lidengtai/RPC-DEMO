package com.taylor.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RPCServer
 * @Description RPC服务提供端
 * @Author lidengtai
 * @Date 2019/6/30 15:26
 * @Version 1.0
 */
public class RPCServer {

    //线程池初始化配置
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5,30,
            200, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(15));

    //定义map集合注册服务
    Map<String,Class<?>> serverRegistryMap = Collections.synchronizedMap(new HashMap<>());
    //端口号
    private int serverPort;

    public RPCServer() {
    }

    public RPCServer(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * 发布服务的方法
     */
    public void start() throws IOException {
        //建立网络通信
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(serverPort));
        System.out.println("rpc服务启动......");
        try {
            while (true){
                executor.execute(new RPCTask(serverSocket.accept()));
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    /**
     * 定义一个暴露服务接口的方法
     * @param serviceInterface
     * @param serviceImpl
     */
    public void  registryServer(Class<?> serviceInterface,Class<?> serviceImpl){
        serverRegistryMap.put(serviceInterface.getName(),serviceImpl);

    }

    /**
     * 关闭服务的方法
     */
    public void  stop(){
        executor.shutdown();
    }


    /**
     * 处理客户端线程代理类
     */
    private class RPCTask implements Runnable{

        private final Socket client;

        public RPCTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {

            //定义反序列化句柄
            ObjectInputStream  deSerializer = null;
            //定义序列化句柄
            ObjectOutputStream serializer = null;
            try {
                //创建一个反序列化句柄
                deSerializer = new ObjectInputStream(client.getInputStream());
                //获取调用接口全名称
                String interfaceName = deSerializer.readUTF();
                //获取调用的方法
                String methodName = deSerializer.readUTF();
                //获取方法参数类型列表
                Class<?>[] paramTypes = (Class<?>[]) deSerializer.readObject();
                //获取方法参数列表
                Object[] parameters = (Object[]) deSerializer.readObject();
                //通过暴露的服务接口获取对应的实现类
                Class<?> serviceInstance = serverRegistryMap.get(interfaceName);
                //反射创建一个方法
                Method method = serviceInstance.getDeclaredMethod(methodName, paramTypes);
                //通过反射调用方法
                Object result = method.invoke(serviceInstance.newInstance(), parameters);
                //把服务端调用处理的结果返回到客户端
                serializer = new ObjectOutputStream(client.getOutputStream());
                //把结果序列化到客户端
                serializer.writeObject(result);

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if (serializer != null) {
                        serializer.flush();
                        serializer.close();
                    }
                }catch (Exception e){
                        e.printStackTrace();
                    }
                try {
                    if(deSerializer != null){
                        deSerializer.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    if (client != null)
                        client.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
}
