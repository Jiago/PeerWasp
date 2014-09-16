package org.peerbox.watchservice.states;

import java.nio.file.Path;

import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.exceptions.IllegalFileLocation;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.peerbox.FileManager;
import org.peerbox.model.H2HManager;
import org.peerbox.watchservice.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * if a move or renaming (which actually is a move at the same path location) occurs,
 * this move state will be assigned. The transition to another state except the delete state
 * will not be accepted.
 * 
 * @author winzenried
 *
 */
public class LocalMoveState extends ActionState {
	
	private final static Logger logger = LoggerFactory.getLogger(LocalMoveState.class);
	
	private Path sourcePath;

	public LocalMoveState(Action action, Path sourcePath) {
		super(action);
		this.sourcePath = sourcePath;
	}
	
	public Path getSourcePath(){
		return sourcePath;
	}
	/**
	 * The transition from Move to Create is not possible and will be denied
	 * 
	 * @return new MoveState object
	 */
	@Override
	public ActionState handleLocalCreateEvent() {
		logger.debug("Create Request denied: Cannot change from Move to Create.");
		return new LocalMoveState(action, getSourcePath());
	}

	/**
	 * If an object gets deleted directly after it has been moved/renamed, the state
	 * changes to Delete
	 * 
	 * @return new DeleteState object
	 */
	@Override
	public ActionState handleLocalDeleteEvent() {
		logger.debug("Delete Request accepted: State changed from Move to Delete.");
		return new LocalDeleteState(action);
	}

	/**
	 * The state transition to Modify is not allowed
	 * 
	 * @return new MoveState object
	 */
	@Override
	public ActionState handleLocalModifyEvent() {
		logger.debug("Modify Request denied: Cannot change from Move to Modify State.");
		//return new MoveState();
		//throw new IllegalStateException("Modify Request denied: Cannot change from Move to Modify State.");
		return this;
	}
	
	@Override
	public ActionState handleLocalMoveEvent(Path oldFilePath) {
		throw new RuntimeException("Not implemented...");
	}

	@Override
	public void execute(FileManager fileManager) throws NoSessionException, NoPeerConnectionException {
		fileManager.move(sourcePath.toFile(), action.getFilePath().toFile());
		logger.debug("Task \"Move File\" executed.");
	}


}