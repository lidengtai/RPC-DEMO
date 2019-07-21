package com.taylor.rpc;

import com.taylor.service.BookService;
import com.taylor.service.impl.BookServiceImpl;

import java.io.IOException;

/**
 * @ClassName RpcTest
 * @Description TODO
 * @Author lidengtai
 * @Date 2019/6/30 16:47
 * @Version 1.0
 */
public class RpcServerTest {
    public static void main(String[] args) {
         RPCServer rpcServer = new RPCServer(12345);
         //配置暴露服务接口
        rpcServer.registryServer(BookService.class,BookServiceImpl.class);
        try {
            rpcServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
