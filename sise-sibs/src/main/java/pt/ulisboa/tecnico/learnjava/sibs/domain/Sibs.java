package pt.ulisboa.tecnico.learnjava.sibs.domain;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	final Services services;
	final HashMap<Integer, TransferOperation> transferOperations;
	// The reason we chose a HashMap data structure is because an ID must be
	// attributed
	// to each Operation Transfer; thus squence will be imporant
	private int transferOperationID = 0;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.transferOperations = new HashMap<Integer, TransferOperation>();
		this.services = services;
	}

	public void transfer(String sourceIban, String targetIban, int amount) throws OperationException, SibsException {

		TransferOperation transferOperation;
		transferOperation = new TransferOperation(sourceIban, targetIban, amount, this.services);

		this.transferOperationID += 1;
		this.transferOperations.put(this.transferOperationID, transferOperation);
		addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount);

	}

	public void processOperations() {
		for (Map.Entry mapElement : this.transferOperations.entrySet()) {
			if (!this.transferOperations.get(mapElement.getKey()).getCanceledState().equals(SCompleted.class)
					|| !this.transferOperations.get(mapElement.getKey()).getCanceledState().equals(SCanceled.class)) {
				this.transferOperations.get(mapElement.getKey()).process();
			}
		}
	}

	public void cancelOperation(int transferOperationCacelID) {
		this.transferOperations.get(transferOperationCacelID).cancel();
	}

	public TransferOperation getTransferOpertation(int transferOperationCacelID) {
		return this.transferOperations.get(transferOperationCacelID);
	}

	// This code was needed for ProjectPart1
//		if (sourceIban.substring(0, 3).equals(targetIban.substring(0, 3))) {
//			this.services.withdraw(sourceIban, amount);
//			this.services.deposit(targetIban, amount);
//			addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount);
//			return 0;
//
//		} else {
//			int comission = 5;
//			this.services.withdraw(sourceIban, amount + comission);
//			this.services.deposit(targetIban, amount);
//			addOperation(Operation.OPERATION_TRANSFER, sourceIban, targetIban, amount);
//			return comission;
//		}

	public int addOperation(String type, String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		Operation operation;
		if (type.equals(Operation.OPERATION_TRANSFER)) {
			operation = new TransferOperation(sourceIban, targetIban, value, this.services);
		} else {
			operation = new PaymentOperation(targetIban, value);
		}

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}
}
