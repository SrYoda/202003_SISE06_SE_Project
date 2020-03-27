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
		String i = scan.nextLine();

		while (!i.equals("exit")) {

			if (i.equals("associate-mbway")) {
				controller.getView().askForIban();
				Scanner ib = new Scanner(System.in);
				String Iban = ib.nextLine();

				controller.getView().askForPhoneNumber();
				Scanner pn = new Scanner(System.in);
				String phoneNumber = pn.nextLine();

				controller.associateMbWay(Iban, phoneNumber);

			} else if (i.equals("confirm-mbway")) {
				controller.getView().pleaseSummitActivationCode();

				Scanner cd = new Scanner(System.in);
				String inputCode = cd.nextLine();

				controller.confirmMbWay(inputCode);

			} else if (i.equals("mbway-transfer")) {

				controller.getView().askForPhoneNumber();
				Scanner srPN = new Scanner(System.in);
				String sourcePhoneNumber = srPN.nextLine();

				controller.getView().askForTargetPhoneNumber();
				Scanner tgPN = new Scanner(System.in);
				String targetPhoneNumber = tgPN.nextLine();

				controller.getView().askForAmount();
				Scanner amt = new Scanner(System.in);
				int amount = amt.nextInt();

				controller.MBWayTransfer(sourcePhoneNumber, targetPhoneNumber, amount);

			} else if (i.equals("mbway-split-bill")) {
				// The phone number of the establishement charging the bill
				controller.getView().askForTargetPhoneNumber();
				Scanner responsiblePN = new Scanner(System.in);
				String targetPhoneNumber = responsiblePN.nextLine();
				controller.setTargetPhoneNumber(targetPhoneNumber);

				// The bill
				controller.getView().askForBill();
				Scanner b = new Scanner(System.in);
				int bill = b.nextInt();

				int soma = 0;
				int counterOfFriends = 0;

				// As for your phoneNumber
				controller.getView().askForPhoneNumber();
				Scanner yPN = new Scanner(System.in);
				String yourPhoneNumber = yPN.nextLine();
				controller.setYourPhoneNumber(yourPhoneNumber);

				// The amount that you want to pay
				controller.getView().askForAmount();
				Scanner amount1 = new Scanner(System.in);
				int amount = amount1.nextInt();

				controller.addTransfer(yourPhoneNumber, amount);

				controller.getView().menuOptionsSplitBill();
				Scanner scan1 = new Scanner(System.in);
				String a = scan1.nextLine();

				while (!a.equals("Terminate")) {

					counterOfFriends += 1;

					// Input friends phone Number
					controller.getView().askForFriendsPhoneNumber();
					Scanner friend = new Scanner(System.in);
					String friendPhoneNumber = friend.nextLine();

					controller.getView().askForAmount();
					Scanner am = new Scanner(System.in);
					int amountfriend = am.nextInt();

					soma += amount;

					controller.addTransfer(yourPhoneNumber, amountfriend);

				}

				controller.MBWaySplitBill(counterOfFriends, bill);
			}

			else if (i.equals("mbway-addfriend")) {
				controller.getView().askForPhoneNumber();
				Scanner yourPhone = new Scanner(System.in);
				String phoneNumber = yourPhone.nextLine();

				controller.getView().askForFriendsPhoneNumber();
				Scanner newFriendPhoneNumber = new Scanner(System.in);
				String friendPhoneNumber = newFriendPhoneNumber.nextLine();

				controller.addFriend(phoneNumber, friendPhoneNumber);

			} else {
				controller.viewInvalidInputMenu();
			}

			controller.viewMenu();
			Scanner scan1 = new Scanner(System.in);
			i = scan.nextLine();

		}

		System.out.println("MBWAY was terminated");
	}
}