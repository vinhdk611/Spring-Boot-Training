package com.testApi.demoApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*generic - tham so hoa du lieu(vd: List<String> abc = ...
vd:
class Box<T> {
   private T value;

   public Box(T value){
       this.value = value;
   }
   public T getVal(){
      return value;
   }
}

ben ngoai ta goi Box a = new Box<String>("hello"); <-- java biết T sẽ là loai String nen cac field co T se la kieu String het

 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//khai bao dung de quan ly cach json tra ve, o day se ko hien thi nhung field json null
public class ApiResponse <T>{ // ky thuat Generics, khai bao <T> giup
    //khai bao <T> de su dung T trong cac field, neu ko co thi cac T field se ko biet minh thuoc data type gi
    private int code;
    private String message;
    private T result; //T-TYPE duoc su dung, data api tra ve rat da dang, ko biet no thuoc kieu nao\
}
