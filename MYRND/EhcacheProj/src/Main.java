public class Main {
	public static void main(String arg[]) throws Exception {
		AlbumCache cache = new AlbumCache();
		findAlbums(cache);
		loadAlbums(cache);
		findAlbums(cache);
	}

	private static void loadAlbums(AlbumCache cache) 
	{
		System.out.println();
		System.out.println("Adding albums.");
		cache.addAlbum(new Album(0, "Moving Pictures", "Rush", 1981));
		cache.addAlbum(new Album(1, "What's Going On", "Marvin Gaye",  
				1971));
		cache.addAlbum(new Album(2, "The White Album", "The Beatles",  
				1968));
		cache.addAlbum(new Album(3, "The Dark Side of the Moon", "Pink Floyd", 1973));
	}

	private static void findAlbums(AlbumCache cache) 
	{
		System.out.println();
		System.out.println("Finding albums:");
		for(int i=0; i<4; i++) 
		{
			System.out.println(cache.findAlbum(i));
		}
	}
}