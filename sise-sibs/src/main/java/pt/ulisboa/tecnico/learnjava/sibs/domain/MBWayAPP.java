package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class MBWayAPP {

	private static final String ADDRESS = "Ave.";

	// I change the verifier in the Create client to Accept numbers with one digit
	// to be easier to test it
	private static final String PHONE_NUMBER1 = "111";
	private static final String PHONE_NUMBER2 = "222";
	private static final String PHONE_NUMBER3 = "333";
	private static final String PHONE_NUMBER4 = "444";
	private static final String PHONE_NUMBER5 = "555";

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

	public static void main(String[] args) throws AccountException, ClientException, BankException {

		// Initialization parameters needed

		Services services = new Services();
		Sibs sibs = new Sibs(100, services);
		Bank sourceBank = new Bank("CGD");
		Bank targetBank = new Bank("BPI");

		Client Client1 = new Client(sourceBank, FIRST_NAME1, LAST_NAME, NIF1, PHONE_NUMBER1, ADDRESS, 33);
		Client Client2 = new Client(sourceBank, FIRST_NAME2, LAST_NAME, NIF2, PHONE_NUMBER2, ADDRESS, 33);
		Client Client3 = new Client(targetBank, FIRST_NAME3, LAST_NAME, NIF3, PHONE_NUMBER3, ADDRESS, 33);
		Client Client4 = new Client(targetBank, FIRST_NAME4, LAST_NAME, NIF4, PHONE_NUMBER4, ADDRESS, 33);
		Client Client5 = new Client(targetBank, FIRST_NAME5, LAST_NAME, NIF5, PHONE_NUMBER5, ADDRESS, 33);

		String iban1 = sourceBank.createAccount(Bank.AccountType.CHECKING, Client1, 1000, 0);
		String iban2 = sourceBank.createAccount(Bank.AccountType.CHECKING, Client2, 1000, 0);

		String iban3 = targetBank.createAccount(Bank.AccountType.CHECKING, Client3, 1000, 0);
		String iban4 = targetBank.createAccount(Bank.AccountType.CHECKING, Client4, 1000, 0);
		String iban5 = targetBank.createAccount(Bank.AccountType.CHECKING, Client5, 1000, 0);

		System.out.println(iban1 + " " + iban2 + " " + iban3 + " " + iban4 + " " + iban5 + "\n\n ");

		///

		MBWayAccount model = new MBWayAccount();
		MBWayAccountView view = new MBWayAccountView();
		MBWayController controller = new MBWayController(model, view, services);

		// Tenho que adicionar os Banco/ Contas /CLientes

		controller.viewMenu();

		Scanner scan = new Scanner(System.in);
		int i = scan.nextInt();

		while (0 != i) {

			if (1 == i) {
				controller.associateMbWay();

			} else if (2 == i) {
				controller.confirmMbWay();

			} else if (3 == i) {
				controller.MBWayTransfer();

			} else if (4 == i) {
				controller.MBWaySplitBill();

			} else if (5 == i) {
				controller.addFriend();

			} else {
				controller.viewInvalidInputMenu();
			}

			controller.viewMenu();
			Scanner scan1 = new Scanner(System.in);
			i = scan1.nextInt();

		}
	}
}