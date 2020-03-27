package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWayAccount;
import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWayAccountView;
import pt.ulisboa.tecnico.learnjava.sibs.domain.MBWayController;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;

public class MBWayControllerTest {

	private static final String ADDRESS = "Ave.";

	// I change the verifier in the Create client to Accept numbers with one digit
	// to be easier to test it
	private static final String PHONE_NUMBER1 = "111111111";
	private static final String PHONE_NUMBER2 = "222222222";
	private static final String PHONE_NUMBER3 = "333333333";
	private static final String PHONE_NUMBER4 = "444444444";
	private static final String PHONE_NUMBER5 = "555555555";

	private static final String NIF1 = "123456789";
	private static final String NIF2 = "123456788";
	private static final String NIF3 = "123456787";
	private static final String NIF4 = "123456786";
	private static final String NIF5 = "123456785";

	private static final String LAST_NAME = "Silva";

	private static final String FIRST_NAME1 = "A";
	private static final String FIRST_NAME2 = "B";
	private static final String FIRST_NAME3 = "C";
	private static final String FIRST_NAME4 = "D";
	private static final String FIRST_NAME5 = "E";

	public MBWayController controller;
	public MBWayAccount model;
	public MBWayAccountView view;

	public String iban1;
	public String iban2;
	public String iban3;
	public String iban4;
	public String iban5;

	public Sibs sibs;
	public Services services;

	public Bank sourceBank;
	public Bank targetBank;

	@Before
	public void setUp() throws ClientException, BankException, AccountException {

		this.services = new Services();
		this.sibs = new Sibs(100, this.services);
		this.sourceBank = new Bank("BES");
		this.targetBank = new Bank("BIP");

		Client Client1 = new Client(this.sourceBank, FIRST_NAME1, LAST_NAME, NIF1, PHONE_NUMBER1, ADDRESS, 33);
		Client Client2 = new Client(this.sourceBank, FIRST_NAME2, LAST_NAME, NIF2, PHONE_NUMBER2, ADDRESS, 33);
		Client Client3 = new Client(this.targetBank, FIRST_NAME3, LAST_NAME, NIF3, PHONE_NUMBER3, ADDRESS, 33);
		Client Client4 = new Client(this.targetBank, FIRST_NAME4, LAST_NAME, NIF4, PHONE_NUMBER4, ADDRESS, 33);
		Client Client5 = new Client(this.targetBank, FIRST_NAME5, LAST_NAME, NIF5, PHONE_NUMBER5, ADDRESS, 33);

		this.iban1 = this.sourceBank.createAccount(Bank.AccountType.CHECKING, Client1, 1000, 0);
		this.iban2 = this.sourceBank.createAccount(Bank.AccountType.CHECKING, Client2, 1000, 0);

		this.iban3 = this.targetBank.createAccount(Bank.AccountType.CHECKING, Client3, 1000, 0);
		this.iban4 = this.targetBank.createAccount(Bank.AccountType.CHECKING, Client4, 1000, 0);
		this.iban5 = this.targetBank.createAccount(Bank.AccountType.CHECKING, Client5, 1000, 0);

		this.model = new MBWayAccount();
		this.view = new MBWayAccountView();
		this.controller = new MBWayController(this.model, this.view, this.services);
	}

	@Test
	public void successAssociateMbway() {
		this.controller.associateMbWay(this.iban2, PHONE_NUMBER2);
		assertEquals(this.iban2, this.model.getIban());
	}

	@Test
	public void successConfirmMbway() {
		this.controller.associateMbWay(this.iban1, PHONE_NUMBER1);
		this.controller.confirmMbWay("111BES");
		assertEquals(1, this.model.getDBMBWayAccounts().size());
	}

	@Test
	public void successTranserMbway() throws AccountException {
		this.controller.associateMbWay(this.iban1, PHONE_NUMBER1);
		this.controller.confirmMbWay("111BES");

		assertEquals(this.controller.getAccount(PHONE_NUMBER1).getIban(), this.iban1);

		this.controller.associateMbWay(this.iban2, PHONE_NUMBER2);
		this.controller.confirmMbWay("222BES");

		assertEquals(this.controller.getAccount(PHONE_NUMBER2).getIban(), this.iban2);

		this.controller.MBWayTransfer(PHONE_NUMBER1, PHONE_NUMBER2, 100);

		assertEquals(this.services.getAccountByIban(this.iban1).getBalance(), 900);
		assertEquals(this.services.getAccountByIban(this.iban2).getBalance(), 1100);

	}

	@Test
	public void successAddFriendrMbway() throws AccountException {
		this.controller.associateMbWay(this.iban1, PHONE_NUMBER1);
		this.controller.confirmMbWay("111BES");

		this.controller.associateMbWay(this.iban2, PHONE_NUMBER2);
		this.controller.confirmMbWay("222BES");

		this.controller.addFriend(PHONE_NUMBER1, PHONE_NUMBER2);

		assertEquals(this.controller.getAccount(PHONE_NUMBER1).numberOfFriends(), 1);
		assertTrue(this.controller.getAccount(PHONE_NUMBER1).verifyIfFriend(PHONE_NUMBER2));
	}

	@Test
	public void sucessSplitBill() throws AccountException {
		this.controller.associateMbWay(this.iban1, PHONE_NUMBER1);
		this.controller.confirmMbWay("111BES");

		assertTrue(this.controller.verifyIfUserOfMBWay(PHONE_NUMBER1));

		this.controller.associateMbWay(this.iban2, PHONE_NUMBER2);
		this.controller.confirmMbWay("222BES");

		this.controller.associateMbWay(this.iban3, PHONE_NUMBER3);
		this.controller.confirmMbWay("333BIP");

		this.controller.associateMbWay(this.iban4, PHONE_NUMBER4);
		this.controller.confirmMbWay("444BIP");

		this.controller.associateMbWay(this.iban5, PHONE_NUMBER5);
		this.controller.confirmMbWay("555BIP");
		assertTrue(this.controller.verifyIfUserOfMBWay(PHONE_NUMBER5));

		this.controller.addFriend(PHONE_NUMBER1, PHONE_NUMBER2);
		this.controller.addFriend(PHONE_NUMBER1, PHONE_NUMBER3);
		this.controller.addFriend(PHONE_NUMBER1, PHONE_NUMBER4);

		assertEquals(this.controller.getAccount(PHONE_NUMBER1).numberOfFriends(), 3);
		assertTrue(this.controller.getAccount(PHONE_NUMBER1).verifyIfFriend(PHONE_NUMBER2));
		assertTrue(this.controller.getAccount(PHONE_NUMBER1).verifyIfFriend(PHONE_NUMBER3));
		assertTrue(this.controller.getAccount(PHONE_NUMBER1).verifyIfFriend(PHONE_NUMBER4));

		this.controller.setTargetPhoneNumber(PHONE_NUMBER5);
		this.controller.addTransfer(PHONE_NUMBER5, 1000);

		this.controller.setYourPhoneNumber(PHONE_NUMBER1);
		this.controller.addTransfer(PHONE_NUMBER1, 300);
		this.controller.addTransfer(PHONE_NUMBER2, 200);
		this.controller.addTransfer(PHONE_NUMBER3, 300);
		this.controller.addTransfer(PHONE_NUMBER4, 200);

		this.controller.MBWaySplitBill(3, 1000);

		assertEquals(this.services.getAccountByIban(this.iban1).getBalance(), 700);
		assertEquals(this.services.getAccountByIban(this.iban2).getBalance(), 800);
		assertEquals(this.services.getAccountByIban(this.iban3).getBalance(), 700);
		assertEquals(this.services.getAccountByIban(this.iban4).getBalance(), 800);
		assertEquals(this.services.getAccountByIban(this.iban5).getBalance(), 2000);

	}

	@After
	public void tearDown() {
		Bank.clearBanks();
		this.services = null;
		this.sibs = null;
		this.controller = null;
	}
}
