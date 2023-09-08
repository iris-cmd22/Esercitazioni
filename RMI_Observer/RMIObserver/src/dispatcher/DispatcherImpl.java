package dispatcher;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import interfaces.IDispatcher;
import interfaces.IObserver;
import interfaces.IReading;

public class DispatcherImpl extends UnicastRemoteObject implements IDispatcher{

    private Vector<IObserver> obsT;
    private Vector<IObserver> obsP;
    private IReading r;
    protected final static long serialVersionUID = 10;

    //Costruttore
    public DispatcherImpl() throws RemoteException{
        obsT= new Vector<IObserver>();
        obsP= new Vector<IObserver>();
    }

    @Override
    public void attach(String tipo, IObserver obs) throws RemoteException{

        System.out.println("[DISPATCHER] Richiesta di attach per "+tipo);
        if(tipo.equals("pressione")){
            obsP.add(obs);
        }else{
            obsT.add(obs);
        }
    }

    @Override
    public synchronized void setReading(IReading r) throws RemoteException{
        this.r=r;
        System.out.println("[DISPATCHER] setReading per "+r.getTipo()+": "+r.getValore());
        try{
            Thread.sleep(1000*((int)Math.random()*5)+1);

            if(r.getTipo().equals("pressione")){
                for(int i=0;i<obsP.size();i++){
                    obsP.get(i).notifyReading();
                }
            }else{
                for(int i=0;i<obsT.size();i++){
                    obsT.get(i).notifyReading();
                }
            }

        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getReading() throws RemoteException{
        System.out.println("[DISPATCHER] getReading per: "+r.getValore());
        return r.getValore();
    }


    
}
