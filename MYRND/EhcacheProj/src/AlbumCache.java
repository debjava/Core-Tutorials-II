import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


public class AlbumCache 
{
	private CacheManager manager = new CacheManager();

	   private Cache getCache() {
	     return manager.getCache("albumCache");
	   }

	   public void addAlbum(Album album) {
	     Cache albumCache = getCache();
	     albumCache.put(new Element(album.getId(), album));
	   }

	   public Album findAlbum(int id)
	   {
	     Cache albumCache = getCache();
	     Element element = albumCache.get(id);
	     if (element != null) 
	     {
	       return (Album) element.getValue();
	     }
	     else 
	     {
	       return null;
	     }
	   }
}
