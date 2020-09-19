package FlowSkeleton;

public class SecondConsumer extends Thread{
    SecondProducer p2;

    public SecondConsumer(SecondProducer p2){
        this.p2=p2;
    }
    public void run(){
        while (p2.stop.get()){
            synchronized (p2.w){
                try{
                    p2.w.wait();
                } catch (Exception ex) {ex.printStackTrace();}
            }
        }
    }
}
