package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.SCanceled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.SCompleted;
import pt.ulisboa.tecnico.learnjava.sibs.domain.SDeposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.SError;
import pt.ulisboa.tecnico.learnjava.sibs.domain.SRegistered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.SWithdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTest_ProjectPart2_6 {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;

	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		this.services = new Services();
		this.sibs = new Sibs(100, this.services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
	}

	// Test Register State
	@Test
	public void Registered()
			throws BankException, AccountException, SibsException, OperationException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.sibs.transfer(sourceIban, targetIban, 100);
		assertEquals(SRegistered.class, this.sibs.getTransferOpertation(1).getCurrentState().getClass());

		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void Withdrawn() throws BankException, AccountException, SibsException, OperationException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.sibs.transfer(sourceIban, targetIban, 100);
		this.sibs.processOperations();

		assertEquals(SWithdrawn.class, this.sibs.getTransferOpertation(1).getCurrentState().getClass());
		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void Deposited() throws BankException, AccountException, SibsException, OperationException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.sibs.transfer(sourceIban, targetIban, 100);
		this.sibs.processOperations();
		this.sibs.processOperations();

		assertEquals(SDeposited.class, this.sibs.getTransferOpertation(1).getCurrentState().getClass());
		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void Completed() throws BankException, AccountException, SibsException, OperationException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.sibs.transfer(sourceIban, targetIban, 100);
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();

		assertEquals(SCompleted.class, this.sibs.getTransferOpertation(1).getCurrentState().getClass());
		assertEquals(894, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void Canceled() throws BankException, AccountException, SibsException, OperationException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		String targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.sibs.transfer(sourceIban, targetIban, 100);
		this.sibs.processOperations();
		this.sibs.processOperations();

		this.sibs.cancelOperation(1);

		assertEquals(SCanceled.class, this.sibs.getTransferOpertation(1).getCurrentState().getClass());
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test(expected = SError.class)
	public void Error() throws BankException, AccountException, SibsException, OperationException, ClientException {
		String sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);

		this.sibs.transfer(sourceIban, "CDGCK2", 100);
		this.sibs.processOperations();
		this.sibs.processOperations();

		assertEquals(SError.class, this.sibs.getTransferOpertation(1).getCurrentState().getClass());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
