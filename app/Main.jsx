import { Check, Move, MoveDiagonal2, Plus, Trash } from "lucide-react-native";
import { useCallback, useEffect, useRef, useState } from "react";
import {
	Modal,
	PanResponder,
	Pressable,
	useWindowDimensions,
	View,
} from "react-native";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import { scale, StyleSheet } from "react-native-size-scaling";
import { VolumeManager } from "react-native-volume-manager";
import SelectorView from "../components/SelectorView";
import { ToastMaker } from "../components/ToastMaker";
import EventsEmitter from "./context/EventsEmitter";
import {
	allCacheKeys,
	deleteCache,
	getCache,
	setCache,
} from "./context/Storage";

const RenderBox = ({ boxObj, storeKey, addBox, deleteBox }) => {
	const [isEditing, setIsEditing] = useState(false);
	const [box, setBox] = useState(boxObj);
	const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } =
		useWindowDimensions();
	const listenerRef = useRef(null);

	useEffect(() => {
		(() => {
			try {
				const saved = getCache(storeKey);
				if (saved) {
					setBox(JSON.parse(saved));
				}
			} catch (e) {
				console.warn("Error loading boxes", e);
			}
		})();
	}, [SCREEN_WIDTH, SCREEN_HEIGHT]);

	const startEditing = () => {
		setIsEditing(true);
		EventsEmitter.emit("editingStarted", box?.id); // Pass the id
	};

	useEffect(() => {
		listenerRef.current = (editingBoxId) => {
			if (
				editingBoxId === "rootClick" ||
				(editingBoxId !== box?.id && isEditing)
			) {
				setIsEditing(false);
			}
		};
		EventsEmitter.on("editingStarted", listenerRef.current);

		return () => {
			EventsEmitter.off("editingStarted", listenerRef.current);
		};
	}, [box?.id, isEditing]);

	// Save to storage
	const saveBox = useCallback(
		(updated) => {
			try {
				setBox(updated);
				setCache(storeKey, JSON.stringify(updated));
			} catch (e) {
				console.log("Save box error", e);
			}
		},
		[setBox],
	);

	const saveBoxView = useCallback(
		(val) => {
			try {
				const updated = {
					...box,
					viewShow: val,
					selected: "one",
				};
				setBox(updated);
				setCache(storeKey, JSON.stringify(updated));
			} catch (e) {
				console.log("Save box error", e);
			}
		},
		[setBox, box],
	);

	// PanResponder for dragging
	const panResponder = PanResponder.create({
		onStartShouldSetPanResponder: () => true,
		onPanResponderMove: (_, gesture) => {
			const updated = {
				...box,
				x: Math.min(
					SCREEN_WIDTH - box.w,
					Math.max(0, box.x + gesture.dx),
				),
				y: Math.min(
					SCREEN_HEIGHT - box.h,
					Math.max(0, box.y + gesture.dy),
				),
			};
			setBox(updated);
		},
		onPanResponderRelease: (_, gesture) => {
			const updated = {
				...box,
				x: Math.min(
					SCREEN_WIDTH - box.w,
					Math.max(0, box.x + gesture.dx),
				),
				y: Math.min(
					SCREEN_HEIGHT - box.h,
					Math.max(0, box.y + gesture.dy),
				),
			};
			saveBox(updated);
		},
	});

	// PanResponder for resizing (bottom-right corner)
	const resizeResponder = PanResponder.create({
		onStartShouldSetPanResponder: () => true,
		onPanResponderMove: (_, gesture) => {
			const updated = {
				...box,
				w: Math.max(60, box.w + gesture.dx),
				h: Math.max(60, box.h + gesture.dy),
			};
			setBox(updated);
		},
		onPanResponderRelease: (_, gesture) => {
			const updated = {
				...box,
				w: Math.max(60, box.w + gesture.dx),
				h: Math.max(60, box.h + gesture.dy),
			};
			saveBox(updated);
		},
	});

	if (!box) return null;

	return (
		<>
			<View
				key={box.id}
				style={[
					styles.box,
					{
						left: box.x,
						top: box.y,
						width: box.w,
						height: box.h,
					},
				]}
			>
				{isEditing && (
					<View
						{...resizeResponder.panHandlers}
						style={[
							styles.resizeHandle,
							{ bottom: -scale(14), right: -scale(14) },
						]}
					>
						<MoveDiagonal2 size={scale(18)} color={"#000"} />
					</View>
				)}
				{isEditing && (
					<View
						{...panResponder.panHandlers}
						style={[
							styles.resizeHandle,
							{ bottom: -scale(14), left: -scale(14) },
						]}
					>
						<Move size={scale(18)} color={"#000"} />
					</View>
				)}
				{isEditing && (
					<Pressable
						onPress={() => setIsEditing(false)}
						style={[
							styles.resizeHandle,
							{ top: -scale(14), right: -scale(14) },
						]}
					>
						<Check size={scale(18)} color={"#0ed100"} />
					</Pressable>
				)}
				{isEditing && (
					<Pressable
						onPress={() => {
							setBox(null);
							deleteBox(storeKey);
						}}
						style={[
							styles.resizeHandle,
							{ top: -scale(14), left: -scale(14) },
						]}
					>
						<Trash size={scale(18)} color={"#ff0000"} />
					</Pressable>
				)}
				<SelectorView
					startEditing={startEditing}
					viewSelected={box.viewShow || "empty"}
					isEditing={isEditing}
					viewChange={saveBoxView}
					storeKey={storeKey}
					viewface={box.selected || "one"}
				/>
			</View>
			{isEditing && (
				<Pressable onPress={addBox} style={styles.addMore}>
					<Plus
						size={scale(35)}
						color={"rgba(25, 167, 233, 1)"}
						style={{ alignSelf: "center" }}
					/>
				</Pressable>
			)}
		</>
	);
};

export default function Main() {
	const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } =
		useWindowDimensions();
	const [boxes, setBoxes] = useState([]);
	const [disableTouch, setDisableTouch] = useState(false);
	const subscription = useRef(null);
	const lastVolume = useRef(0);

	useEffect(() => {
		async function init() {
			subscription.current = VolumeManager.addVolumeListener(
				({ volume }) => {
					if (volume > lastVolume.current && !disableTouch) {
						setDisableTouch(true);
						ToastMaker("🔒 Touch off", "");
					} else if (volume < lastVolume.current && disableTouch) {
						ToastMaker("🔓 Touch on", "");
						setDisableTouch(false);
					}
					lastVolume.current = volume;
				},
			);
		}
		init();
		return () => {
			subscription.current?.remove();
		};
	}, [disableTouch]);

	const initFunc = async () => {
		const keys = allCacheKeys();
		const filteredKeys = keys.filter((key) => key.startsWith("boxPos_"));

		if (filteredKeys.length === 0) return;

		const stores = filteredKeys.map((key) => {
			const value = getCache(key);
			return JSON.parse(value);
		});
		setBoxes(stores);
	};

	const saveBoxes = useCallback(
		(storeKey, updated) => {
			try {
				setCache(storeKey, JSON.stringify(updated));
			} catch (e) {
				console.log("Save box error", e);
			}
		},
		[setBoxes],
	);

	const addBoxAtCenter = () => {
		if (boxes.length > 6) {
			return;
		}
		const boxWidth = scale(120);
		const boxHeight = scale(120);
		const id = Date.now().toString();

		const newBox = {
			id: id,
			x: (SCREEN_WIDTH - boxWidth) / 2,
			y: (SCREEN_HEIGHT - boxHeight) / 2,
			w: boxWidth,
			h: boxHeight,
			color: "limegreen",
			storeKey: `boxPos_${id}`,
			viewShow: "empty",
			selected: "one",
		};

		setBoxes((prev) => {
			saveBoxes(`boxPos_${id}`, newBox);
			return [...prev, newBox];
		});
	};

	const deleteBox = useCallback(
		(storeKey) => {
			deleteCache(storeKey);
			setBoxes((prev) => prev.filter((box) => box.storeKey !== storeKey));
		},
		[setBoxes],
	);

	useEffect(() => {
		initFunc();
		ToastMaker("Vol ↑: Touch Lock, Vol ↓: Touch Unlock", "long");
		VolumeManager.getVolume().then((v) => {
			lastVolume.current = v.volume;
		});
	}, []);

	return (
		<GestureHandlerRootView style={styles.container}>
			<Pressable
				onPress={() =>
					EventsEmitter.emit("editingStarted", "rootClick")
				}
				style={{ flex: 1 }}
			>
				{boxes?.map((box, index) => (
					<RenderBox
						boxObj={box}
						storeKey={box.storeKey}
						key={index}
						addBox={addBoxAtCenter}
						deleteBox={deleteBox}
					/>
				))}
				{boxes.length === 0 && (
					<Pressable onPress={addBoxAtCenter} style={styles.addMore}>
						<Plus
							size={scale(35)}
							color={"rgba(25, 167, 233, 1)"}
							style={{ alignSelf: "center" }}
						/>
					</Pressable>
				)}
				<Modal visible={disableTouch} transparent />
			</Pressable>
		</GestureHandlerRootView>
	);
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		backgroundColor: "#000",
	},
	box: {
		position: "absolute",
	},
	resizeHandle: {
		width: 34,
		height: 34,
		backgroundColor: "rgba(255,255,255,0.8)",
		position: "absolute",
		justifyContent: "center",
		alignItems: "center",
		zIndex: 9999,
		borderRadius: 1000,
	},
	addMore: {
		borderColor: "rgba(25, 167, 233, 1)",
		marginLeft: 8,
		width: 50,
		height: 50,
		justifyContent: "center",
		alignSelf: "center",
		top: "45%",
		borderRadius: 10,
		borderWidth: 2,
	},
	disableTouch: {
		backgroundColor: "#FF9535",
		marginRight: 8,
		width: 40,
		height: 40,
		justifyContent: "center",
		position: "absolute",
		bottom: 0,
		right: 0,
	},
});
