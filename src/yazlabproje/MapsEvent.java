/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlabproje;

/**
 *
 * @author Hp
 */
public interface MapsEvent<T> {

    void handle(T event);
}
