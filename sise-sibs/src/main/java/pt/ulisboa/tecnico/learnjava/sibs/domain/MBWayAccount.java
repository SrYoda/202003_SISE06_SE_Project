package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.HashMap;
import java.util.LinkedList;

public class MBWayAccount {

	private HashMap<String, MBWayAccount> DBMBWayAccounts = new HashMap<String, MBWayAccount>();

	private String phonenumber;
	private String Iban;

	private String activationCode;

	private LinkedList<String> friends = new LinkedList<String>();

	public String getPhonenumber() {
		return this.phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getIban() {
		return this.Iban;
	}

	public void setIban(String Iban) {
		this.Iban = Iban;
	}

	public String generateActivationCode() {
		this.activationCode = this.phonenumber.substring(0, 3) + this.Iban.substring(0, 3);
		return this.activationCode = this.phonenumber.substring(0, 3) + this.Iban.substring(0, 3);
	}

	public String getActivationCode() {
		return this.activationCode;
	}

	public void addAccount(String phoneNumber, MBWayAccount account) {
		MBWayAccount newAccount = new MBWayAccount();

		newAccount.setIban(getIban());
		newAccount.setPhonenumber(getPhonenumber());

		this.DBMBWayAccounts.put(phoneNumber, newAccount);
	}

	public void updateAccount(String phoneNumber, MBWayAccount account) {
		this.DBMBWayAccounts.put(phoneNumber, account);
	}

	public MBWayAccount getAnAccount(String phoneNumber) {
		return this.DBMBWayAccounts.get(phoneNumber);
	}

	public void addFriend(String friendPhoneNumber) {
		this.friends.addLast(friendPhoneNumber);
	}

	public int numberOfFriends() {
		return this.friends.size();
	}

	public boolean verifyIfFriend(String friendPhoneNumber) {
		return this.friends.contains(friendPhoneNumber);
	}

	public HashMap getDBMBWayAccounts() {
		return this.DBMBWayAccounts;
	}

}
