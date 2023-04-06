package io.fluentqa.basic.datatypes;

/**
 * @author Patrick
 * float issue:
 * The reason why there's such issue is because a computer works only in discrete mathematics,
 * because the microprocessor can only represent internally full numbers, but no decimals.
 * Because we cannot only work with such numbers, but also with decimals, to circumvent that,
 * decades ago very smart engineers have invented the floating point representation, normalized as IEEE754.
 **/
public class TypesTrickDemo {
    public static void main(String[] args) {
//        float的范围为-2^128 ~ +2^128，也即-3.40E+38 ~ +3.40E+38；
//        double的范围为-2^1024 ~ +2^1024，也即-1.79E+308 ~ +1.79E+308。 float和double的精度是由尾数的位数来决定的
//        float a = 1.2f; //error
//        System.out.println(a);
//        System.out.println();
        float a = 1;
        float b = 1.2f;
        float c = 12e-1f;
        float x = 1.2f;
        double y = x;

        System.out.println("float a=1 shows a= " + a + "\nfloat b=1.2f shows b= " + b + "\nfloat c=12e-1f shows c= " + c + "\nfloat x=1.2f shows x= " + x + "\ndouble y=x shows y= " + y);
    }
}
