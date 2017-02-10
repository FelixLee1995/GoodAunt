package space.ahaha.project;

/**
 * Created by lf729 on 2016/12/23.
 */
public class Address {
    int id;
    int isDefault;
    String name;
    String phone;
    String address;
    String address_sub;

    Address(int id,String name,String phone,String address,String address_sub,int isDefault){
        this.id=id;
        this.name=name;
        this.phone=phone;
        this.address=address;
        this.isDefault=isDefault;
        this.address_sub=address_sub;
    }
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getPhone(){
        return this.phone;
    }
    public String getAddress(){
        return this.address;
    }
    public String getAddress_sub(){
        return this.address_sub;
    }

}
