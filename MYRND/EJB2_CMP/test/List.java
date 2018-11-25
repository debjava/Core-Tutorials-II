import org.jboss.docs.cmp.cd.interfaces.CD;
import org.jboss.docs.cmp.cd.interfaces.CDHome;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import java.util.Hashtable;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * This is a simple client for the "CD" EJB; it lists (to standard output) all
 * the "CD" instances in the system. The "main" method allows this class to be
 * run from the command line.
 */
public class List
{

   public static void main(String[] args)
   {
      // Enclosing the whole process in a single "try" block is not an ideal way
      // to do exception handling, but I don't want to clutter the program up
      // with catch blocks
      try
      {
         // Get a naming context
         InitialContext jndiContext = new InitialContext();

         // Get a reference to a CD Bean
         Object ref  = jndiContext.lookup("cd/CDCollection");

//         CDCollectionHome home = (CDCollectionHome)
//            PortableRemoteObject.narrow (ref, CDCollectionHome.class);
//
//         CDCollection cdCollection = home.create();
//
//         CD[] cds = cdCollection.findAll();
//         for (int i = 0; i < cds.length; i++)
//         {
//            System.out.println (cds[i].getId() + "\t" + cds[i].getTitle() + "\t" +
//               cds[i].getArtist() + "\t" + cds[i].getType());
//         }
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }

}
