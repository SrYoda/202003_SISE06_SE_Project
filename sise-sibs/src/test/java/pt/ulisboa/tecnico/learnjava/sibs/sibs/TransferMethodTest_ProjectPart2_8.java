package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ServicesException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.SError;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodTest_ProjectPart2_8 {
	Services services = mock(Services.class);

	@Test
	public void registeredToError() throws BankException, AccountException, SibsException, OperationException,
			ClientException, ServicesException {

		Sibs sibs = new Sibs(100, this.services);
		String sourceIban = "CGKCK1";
		String targetIban = "CGKK1";

		sibs.transfer(sourceIban, targetIban, 100);

		sibs.setServiceToNull(1);

		sibs.processOperations();
		sibs.processOperations();
		sibs.processOperations();

		assertEquals(SError.class, sibs.getTransferOpertation(1).getCurrentState().getClass());
	}

	@Test
	public void withdrawToError() throws BankException, AccountException, SibsException, OperationException,
			ClientException, ServicesException {
		Sibs sibs = new Sibs(100, this.services);
		String sourceIban = "CGKCK1";
		String targetIban = "CGKK1";

		sibs.transfer(sourceIban, targetIban, 100);

		sibs.processOperations();

		sibs.setServiceToNull(1);

		sibs.processOperations();
		sibs.processOperations();
		sibs.processOperations();

		assertEquals(SError.class, sibs.getTransferOpertation(1).getCurrentState().getClass());
	}

	@Test
	public void depositedToError() throws BankException, AccountException, SibsException, OperationException,
			ClientException, ServicesException {
		Sibs sibs = new Sibs(100, this.services);
		String sourceIban = "CGKCK1";
		String targetIban = "SEEK1";

		sibs.transfer(sourceIban, targetIban, 100);

		sibs.processOperations();
		sibs.processOperations();

		sibs.setServiceToNull(1);

		sibs.processOperations();
		sibs.processOperations();
		sibs.processOperations();

		assertEquals(SError.class, sibs.getTransferOpertation(1).getCurrentState().getClass());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
