
package com.mycompany.foodify.Admin.Manager;

import com.mycompany.foodify.Admin.Manager.foodifyDashboard;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xianc
 */
public class openFoodify {
    public static void main(String[] args) {
        openFoodifyFrame of = new openFoodifyFrame();
        foodifyDashboard fd = new foodifyDashboard();
        of.setVisible(true);
        
        for(int i = 1; i<= 100; ++i) {
            try {
                Thread.sleep(80);
                of.progressBar.setValue(i);
                
                if(i%2==0) {
                    of.pleaseWait.setText("Please Wait..");
                }else{
                    of.pleaseWait.setText("Please Wait...");
                }
                
                if(i == 100) {
                    of.setVisible(false);
                    fd.setVisible(true);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(openFoodify.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
