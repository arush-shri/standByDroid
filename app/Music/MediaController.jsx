import { NativeModules } from "react-native";

const { MediaControllerModule } = NativeModules;

export const MediaController = {
	getNowPlaying: () => MediaControllerModule.getNowPlaying(),
	openNotificationAccessSettings: () =>
		MediaControllerModule.openNotificationAccessSettings(),
	sendCommand: (action) => MediaControllerModule.sendCommand(action),
	getPlaybackPosition: () => MediaControllerModule.getPlaybackPosition(),
	seekTo: (position) => MediaControllerModule.seekTo(position),
};

export const GetCurrTrack = async () => {
	return await MediaController.getNowPlaying();
};

export const PlayMedia = async () => {
	await MediaController.sendCommand("play");
	return true;
};

export const PauseMedia = async () => {
	await MediaController.sendCommand("pause");
	return true;
};

export const SendCommand = async (command) => {
	await MediaController.sendCommand(command);
	return true;
};
