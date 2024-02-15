package androidsamples.java.dicegames;

import java.util.List;
import java.util.Vector;

import androidx.lifecycle.ViewModel;

/**
 * A {@link ViewModel} for the gambling game that allows the user to choose a game type, set a wager, and then play.
 */
public class TwoOrMoreViewModel extends ViewModel {

  /**
   * No argument constructor.
   */
  private Die cube1;
  private Die cube2;
  private Die cube3;
  private Die cube4;
  private int currency;
  private int betAmt;
  private int cubeCount;
  private GameResult resultOfGame;
  private GameType typeOfGame;
  private boolean flag;

  public TwoOrMoreViewModel() {
    // TODO implement method
    currency = 0;
    betAmt = 0;
    cubeCount = 0;
    resultOfGame = GameResult.UNDECIDED;
    typeOfGame = GameType.NONE;
    flag = false;
  }

  /**
   * Reports the current balance.
   *
   * @return the balance
   */
  public int balance() {
    // TODO implement method
  return currency;
  }

  /**
   * Sets the balance to the given amount.
   *
   * @param balance the given amount
   */
  public void setBalance(int balance) {
      currency = balance;
    // TODO implement method
  }

  /**
   * Reports the current game type as one of the values of the {@code enum} {@link GameType}.
   *
   * @return the current game type
   */
  public GameType gameType() {
    // TODO implement method
    return typeOfGame;
  }

  /**
   * Sets the current game type to the given value.
   *
   * @param gameType the game type to be set
   */
  public void setGameType(GameType gameType) {
    // TODO implement method
   this.typeOfGame = gameType;
  }

  /**
   * Reports the wager amount.
   *
   * @return the wager amount
   */
  public int wager() {
    // TODO implement method
    return betAmt;
  }

  /**
   * Sets the wager to the given amount.
   *
   * @param wager the amount to be set
   */
  public void setWager(int wager) {
    // TODO implement method
    betAmt = wager;
  }

  /**
   * Reports whether the wager amount is valid for the given game type and current balance.
   * For {@link GameType#TWO_ALIKE}, the balance must be at least twice as much, for {@link GameType#THREE_ALIKE}, at least thrice as much, and for {@link GameType#FOUR_ALIKE}, at least four times as much.
   * The wager must also be more than 0.
   *
   * @return {@code true} iff the wager set is valid
   */
  public boolean isValidWager() {
    // TODO implement method
    if(betAmt<=0)
      return false;
    if((gameType() == GameType.TWO_ALIKE) && (currency >= (2*betAmt)))
      return true;
    else if((gameType() == GameType.THREE_ALIKE) && (currency >= (3*betAmt)))
      return true;
    else if((gameType() == GameType.FOUR_ALIKE) && (currency >= (4*betAmt)))
      return true;
    else
      return false;
  }

  /**
   * Returns the current values of all the dice.
   *
   * @return the values of dice
   */
  public List<Integer> diceValues() {
    // TODO implement method
    List<Integer> die_values;
    die_values = new Vector<Integer>();
    die_values.add(cube1.value());
    die_values.add(cube2.value());
    die_values.add(cube3.value());
    die_values.add(cube4.value());

    return die_values;

  }

  /**
   * Adds the given {@link Die} to the game.
   *
   * @param d the Die to be added
   */
  public void addDie(Die d) {
    // TODO implement method
    if(cubeCount==0)
    {
      cube1 = d;
      cubeCount++;
    }
    else if(cubeCount==1)
    {
      cube2 = d;
      cubeCount++;
    }
    else if(cubeCount==2)
    {
      cube3 = d;
      cubeCount++;
    }
    else if(cubeCount==3)
    {
      cube4 = d;
      cubeCount++;
    }
  }

  /**
   * Simulates playing the game based on the type and the wager and reports the result as one of the values of the {@code enum} {@link GameResult}.
   *
   * @return result of the current game
   * @throws IllegalStateException if the wager or the game type was not set to a proper value.
   */
  public GameResult play() throws IllegalStateException {
    // TODO implement method

    if (typeOfGame == null || typeOfGame == GameType.NONE) {
      throw new IllegalStateException("Game Type not set, can't play!");
    }
    if (!isValidWager() && betAmt == 0) {
      throw new IllegalStateException("Wager not set, can't play!");
    }
    if (!isValidWager() && betAmt != 0) {
      throw new IllegalStateException("Wager amount invalid, can't play!");
    }
    if (cubeCount < 4) {
      throw new IllegalStateException("Not enough dice, can't play!");
    }
    cube1.roll();
    cube2.roll();
    cube3.roll();
    cube4.roll();
    boolean check1 = cube1.value() == cube2.value();
    boolean check2 = cube1.value() == cube3.value();
    boolean check3 = cube1.value() == cube4.value();
    boolean check4 = cube2.value() == cube3.value();
    boolean check5 = cube2.value() == cube4.value();
    boolean check6 = cube3.value() == cube4.value();
    if (typeOfGame == GameType.TWO_ALIKE) {
      if ((check1 && !check6) || (!check1 && check6)) {
        setBalance(currency + (betAmt * 2));
        resultOfGame = GameResult.WIN;
      } else if ((check2 && !check5) || (!check2 && check5)) {
        setBalance(currency + (betAmt * 2));
        resultOfGame = GameResult.WIN;
      } else if ((check3 && !check4) || (!check3 && check4)) {
        setBalance(currency + (betAmt * 2));
        resultOfGame = GameResult.WIN;
      } else if (check1 || check2 || check3) {
        setBalance(currency + (betAmt * 2));
        resultOfGame = GameResult.WIN;
      } else {
        setBalance(currency - (betAmt * 2));
        resultOfGame = GameResult.LOSS;
      }
    } else if (typeOfGame == GameType.THREE_ALIKE) {
      if (check1 && check4) {
        setBalance(currency + (betAmt * 3));
        resultOfGame = GameResult.WIN;
      } else if (check1 && check5) {
        setBalance(currency + (betAmt * 3));
        resultOfGame = GameResult.WIN;
      } else if (check2 && check6) {
        setBalance(currency + (betAmt * 3));
        resultOfGame = GameResult.WIN;
      } else if (check4 && check6) {
        setBalance(currency + (betAmt * 3));
        resultOfGame = GameResult.WIN;
      } else {
        setBalance(currency - (betAmt * 3));
        resultOfGame = GameResult.LOSS;
      }
    } else if (typeOfGame == GameType.FOUR_ALIKE) {
      if (check1 && check4 && check6 && check2 && check5 && check3) {
        setBalance(currency + (betAmt * 4));
        resultOfGame = GameResult.WIN;
      } else {
        setBalance(currency - (betAmt * 4));
        resultOfGame = GameResult.LOSS;
      }
    }
    return resultOfGame;
  }

  public void flagSet(boolean x) {
    flag = x;
  }

  public boolean flagReturn() {
    return flag;
  }
}
