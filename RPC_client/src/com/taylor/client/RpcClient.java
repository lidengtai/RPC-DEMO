package com.taylor.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @ClassName RpcClient
 * @Description TODO
 * @Author lidengtai
 * @Date 2019/6/30 17:20
 * @Version 1.0
 */
public class RpcClient {

    /**
     * @param interfaceClass
     * @param addr
     * @param <T>
     * @return
     */
    public <T> T getRemoteProxy(Class<?> interfaceClass, InetSocketAddress addr){

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //socket句柄
                Socket socket = null;
                //定义反序列化句柄
                ObjectInputStream deSerializer = null;
                //定义序列化句柄
                ObjectOutputStream serializer = null;
                try {
                    //创建客户端到服务端的连接
                    socket = new Socket();
                    socket.connect(addr);
                    //创建序列化对象
                    serializer = new ObjectOutputStream(socket.getOutputStream());
                    serializer.writeUTF(interfaceClass.getName());
                    serializer.writeUTF(method.getName());
                    serializer.writeObject(method.getParameterTypes());
                    serializer.writeObject(args);
                    //创建反序列化对象
                    deSerializer = new ObjectInputStream(socket.getInputStream());
                    return deSerializer.readObject();
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
                        if (socket != null)
                            socket.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }
}
