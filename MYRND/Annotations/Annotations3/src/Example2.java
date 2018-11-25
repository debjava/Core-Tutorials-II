@License(
name = "Apache",
notice = "license notice ........",
redistributable = true,
trademarks =
   {@Trademark(description="abcd",owner="xyz"),
    @Trademark(description="efgh",owner="klmn")}
)
public class Example2 {
    public static void main(String []args) {
    License lic;
    lic=Example2.class.getAnnotation(License.class);
    System.out.println(lic.name());
    System.out.println(lic.notice());
    System.out.println(lic.redistributable());
    Trademark [] tms = lic.trademarks();
    for(Trademark tm : tms) {
        System.out.println(tm.description());
        System.out.println(tm.owner());
        }
    }
}