/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.user.manager.repository;

import com.hazelcast.core.HazelcastInstance;
import hu.mi.jwt.model.key.KeyStore;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author parisek
 */
@Repository
public class HazelcastKeyStore implements KeyStore {
    
    @Autowired
    private HazelcastInstance hazelcastInstance;
    
    @Override
    public Map getMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }
}