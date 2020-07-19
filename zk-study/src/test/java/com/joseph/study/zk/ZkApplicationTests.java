package com.joseph.study.zk;

import org.I0Itec.zkclient.*;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class ZkApplicationTests {

    private ZkClient zkClient ;

    public void before() {

    }

    @Test
    public void contextLoads() {
        ZkClient zkClient = new ZkClient("192.168.1.240:2181");
        String path = "/name";

        zkClient.deleteRecursive(path);
        boolean exists = zkClient.exists(path);
        if (!exists) {
            zkClient.createPersistent(path);
        }

        Map.Entry<List<ACL>, Stat> acl = zkClient.getAcl(path);
        System.out.println("test " + acl);

        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("print all children of " + parentPath + " , size: " + currentChilds.size());
                for(String child: currentChilds) {
                    System.out.println("\t child: " +child);
                }
            }
        });

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                doHandleDataChange( dataPath, data);
            }

            private void doHandleDataChange(String dataPath, Object data) {
                System.out.println("dataPath: " + dataPath + " data: " + data.toString());
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("dataPath: " + dataPath + " was deleted " );
            }
        });


        zkClient.subscribeStateChanges(new IZkStateListener(){

            @Override
            public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
                doHandleStateChanged(keeperState);
            }

            @Override
            public void handleNewSession() throws Exception {
                System.out.println("zookeeper new session created");
            }

            @Override
            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {
                System.out.println(ExceptionUtils.readStackTrace(throwable));
            }
        });

    }
    
    private void doHandleStateChanged(Watcher.Event.KeeperState keeperState) {
        System.out.println(Watcher.Event.KeeperState.class.getClass().getName() + " stateChanged!");
        switch (keeperState) {
            case Unknown:
                System.out.println(Watcher.Event.KeeperState.Unknown);
                break;
            case Disconnected:
                System.out.println(Watcher.Event.KeeperState.Disconnected);
                break;
            case NoSyncConnected:
                System.out.println(Watcher.Event.KeeperState.NoSyncConnected);
                break;
            case SyncConnected:
                System.out.println(Watcher.Event.KeeperState.SyncConnected);
                break;
            case AuthFailed:
                System.out.println(Watcher.Event.KeeperState.AuthFailed);
                break;
            case ConnectedReadOnly:
                System.out.println(Watcher.Event.KeeperState.ConnectedReadOnly);
                break;
            case SaslAuthenticated:
                System.out.println(Watcher.Event.KeeperState.SaslAuthenticated);
                break;
            case Expired:
                System.out.println(Watcher.Event.KeeperState.Expired);
                break;
            default :
                System.out.println("default");
                break;
        }
    }

}
