package a3.quinzical.backend;

//Java API dependencies.
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used to create a module that represents the record of a player, 
 * including the selectable category, their questions and the winnings of the player.
 * 
 * This class uses PracticeDatabase class to construct itself if a readable file that 
 * records the player's data does not exist.
 * 
 * Note that it is using the Singleton pattern because in a single instance of a game, we would
 * only want to instantiate a single object for GameDatabase.
 * 
 * @author Shrey Tailor, Jason Wang
 */
public class GameDatabase {
	
	// Fields belonging to the static context.
	private static GameDatabase _gameDatabase;	
	private final static File _gameFile = new File(System.getProperty("user.dir")+"/GameData.txt");
	
	// Fields belonging to the non-static context.
    private List<Category> _categories = new ArrayList<Category>();
	private final static int _startPrice = 100;
	private final static int _priceIncrement = 100;	
	private final static int _cateNum = 5;
	private final static int _clueNum = 5;
	private int _winning = 0;
	
	/**
     * The only constructor for GameDatabase object which is private, because it can only be
     * accessed by the getInstance method (according to the principles of Singleton pattern).
     */
    private GameDatabase() {
        initialize();
    }
    
    /**
     * This method is used to return the singleton object of the GameDatabase object.
     * @return GameDatabase the instance of our game database.
     */
    public static GameDatabase getInstance() {
        if (_gameDatabase == null) {
        	_gameDatabase = new GameDatabase();
        	try {
				IO.writeGameData(_gameDatabase);
			} catch (IOException e) {
				System.out.println("Error creating GameData.txt");
				e.printStackTrace();
			}
        }

        return _gameDatabase;
    }
    
    /**
     * This method is used to initialize the instance of GameDatabase object.
     * The method reads in a file that represents the player's data and all categories and 
     * clues from a specified file and store them in their     * respective category objects
     * and clue objects. 
     * If the file does not exist then this method will randomly select 5 categories with 5 
     * questions from PracticeDatabase and create a file to store the player's record.
     */
    private void initialize() {
		try {
			if (_gameFile.exists() && _gameFile.isFile()) {
				BufferedReader br = new BufferedReader(new FileReader(_gameFile));
				String line;
				Category newCate = null;
				Clue newClue = null;
				int price = _startPrice;
				while(( line = br.readLine()) != null) {
					line = line.trim();
					if (line.matches("\\d+")) {
						_winning = Integer.parseInt(line);
					}else if(!line.contains("(") && !line.contains(")") && !line.isBlank()){
						price = _startPrice;
						newCate = new Category(line);
						_categories.add(newCate);
					}else if(!line.isBlank()){
						newClue = new Clue(line.split("[(]")[0].trim().replace(",", "").replace("@", ""), line.split("[)]")[1].trim());
						newClue.setPrize(price);
						if(line.startsWith("@")) {
							newClue.setCurrentQuestionTrue();
						}
						newCate.addClue(newClue);
						price += _priceIncrement;
					}
				}
				br.close();		
			}
			else {
				Random rand = new Random();
				Category newCate, selectedCate = null;
				Clue newClue = null;
				
				for(int i = 0; i < _cateNum; i++) {
					int cateIndex = rand.nextInt(PracticeDatabase.getInstance().getCateSize());
					selectedCate = PracticeDatabase.getInstance().getCategory(cateIndex);
					newCate = new Category(selectedCate.getName());
					int price = _startPrice;
					for(int j = 0; j < _clueNum; j++) {
						int questIndex = rand.nextInt(selectedCate.getClueSize());
						newClue = new Clue(selectedCate.getClue(questIndex).getQuestion(), selectedCate.getClue(questIndex).getAnswer());
						newClue.setPrize(price);
						if(j == 0) {
							newClue.setCurrentQuestionTrue();
						}
						newCate.addClue(newClue);
						selectedCate.removeClue(questIndex);
						price += _priceIncrement;
					}
					_categories.add(newCate);
					PracticeDatabase.getInstance().removeCategory(cateIndex);
				}
				PracticeDatabase.kill();
			}
		}catch(IOException e) {
			System.out.println("Error occured during reading GameData file");
		}
    }

    /**
     * This method is used to change the current winning of the player.
     * @param prize the winning to change to.
     */
	public void setWinning(int prize) {
		_winning = prize;
	}
	
	/**
	 * This method is used to get the current prize of the player.
	 * @return _winning the current winning of the player.
	 */
	public int getWinning() {
		return _winning;
	}
	
	/**
     * This method is used to get a particular category from the list of categories in our database.
     * @param position the position of category which we are trying to extract from our list.
     * @return Category the category that is returned from the particular position.
     * @throws IndexOutOfBoundsException this exception is returned if the position requested by
     * the user is out of the valid range of the list.
     */
    public Category getCategory(int position) throws IndexOutOfBoundsException {
        return _categories.get(position);
    }
    
    /**
     * This is a method that will return the number of categories in GameDatabase.
     * @return the number of categories.
     */
	public int getCateSize() {
		return _categories.size();
	}
	
    /**
     * This is a method that will dereference the _gameDatabase field, so that when the 
     * getInstance is called next time the GameDatabase would be reloaded.
     */
	public static void kill() {
		_gameDatabase = null;
    }
}