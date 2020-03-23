package pt.ulisboa.tecnico.learnjava.sibs.operation;

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
import pt.ulisboa.tecnico.learnjava.sibs.domain.SRegistered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.SWithdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransderOperation_Project_Part2_Test {
	private static final int VALUE = 100;

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
	private Client targetClientSameBank;
	private Services services;

	private String sourceIban;
	private String targetIban;
	private String targetIbanSameBank;

	private TransferOperation operation;
	private TransferOperation operationSameBank;

	@Before
	public void setUp() throws BankException, AccountException, ClientException, OperationException {
		this.services = new Services();
		this.sibs = new Sibs(100, this.services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
		this.targetClientSameBank = new Client(this.sourceBank, "Rui", "Santos", "270642781", "912890249", "Leiria",
				44);
		this.sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		this.targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.targetIbanSameBank = this.sourceBank.createAccount(Bank.AccountType.CHECKING, this.targetClientSameBank,
				1000, 0);
		this.operation = new TransferOperation(this.sourceIban, this.targetIban, VALUE, this.services);
		this.operationSameBank = new TransferOperation(this.sourceIban, this.targetIbanSameBank, VALUE, this.services);
	}

	@Test
	public void registeredState() throws OperationException, AccountException {

		assertEquals(SRegistered.class, this.operation.getCurrentState().getClass());

	}

	@Test
	public void withdrawnState() throws OperationException, AccountException {

		this.operation.process();

		assertEquals(SWithdrawn.class, this.operation.getCurrentState().getClass());
		assertEquals(900, this.services.getAccountByIban(this.sourceIban).getBalance());

	}

	@Test
	public void depositedState() throws OperationException, AccountException {

		this.operation.process();
		this.operation.process();

		assertEquals(SDeposited.class, this.operation.getCurrentState().getClass());
		assertEquals(1100, this.services.getAccountByIban(this.targetIban).getBalance());

	}

	@Test
	public void completedStateDiffrentBanks() throws OperationException, AccountException {

		this.operation.process();
		this.operation.process();
		this.operation.process();

		assertEquals(SCompleted.class, this.operation.getCurrentState().getClass());
		assertEquals(894, this.services.getAccountByIban(this.sourceIban).getBalance());

	}

	@Test
	public void completedStateSameBanks() throws OperationException, AccountException {

		assertEquals(SRegistered.class, this.operationSameBank.getCurrentState().getClass());

		this.operationSameBank.process();
		assertEquals(SWithdrawn.class, this.operationSameBank.getCurrentState().getClass());

		this.operationSameBank.process();
		assertEquals(SCompleted.class, this.operationSameBank.getCurrentState().getClass());
		assertEquals(900, this.services.getAccountByIban(this.sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(this.targetIbanSameBank).getBalance());

	}

	@Test
	public void cancelOperation() throws OperationException, AccountException {

		this.operation.cancel();

		assertEquals(SCanceled.class, this.operation.getCurrentState().getClass());
		assertEquals(1000, this.services.getAccountByIban(this.sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(this.targetIban).getBalance());

	}

	@Test
	public void cancelOperationWithdrawnState() throws OperationException, AccountException {

		this.operation.process();

		assertEquals(SWithdrawn.class, this.operation.getCurrentState().getClass());

		this.operation.cancel();

		assertEquals(SCanceled.class, this.operation.getCurrentState().getClass());
		assertEquals(1000, this.services.getAccountByIban(this.sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(this.targetIban).getBalance());

	}

	@Test
	public void cancelOperationDepositedState() throws OperationException, AccountException {

		this.operation.process();
		this.operation.process();

		assertEquals(SDeposited.class, this.operation.getCurrentState().getClass());

		this.operation.cancel();

		assertEquals(1000, this.services.getAccountByIban(this.sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(this.targetIban).getBalance());

		assertEquals(SCanceled.class, this.operation.getCurrentState().getClass());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
