import AsyncStorage from "@react-native-async-storage/async-storage";
import { Check, Move, MoveDiagonal2, Plus, Trash } from "lucide-react-native";
import { useCallback, useEffect, useState } from "react";
import {
    PanResponder,
    Pressable,
    useWindowDimensions,
    View
} from "react-native";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import { scale, StyleSheet } from "react-native-size-scaling";

const STORAGE_KEY = "boxesPosition";

export default function Main() {
    const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = useWindowDimensions();
    const [boxes, setBoxes] = useState([]);
    const [editingBoxId, setEditingBoxId] = useState(null);

    // Load from storage
    useEffect(() => {
        (async () => {
            try {
                const saved = await AsyncStorage.getItem(STORAGE_KEY);
                if (saved) {
                    setBoxes(JSON.parse(saved));
                } else {
                    // No storage, define initial boxes based on orientation
                    const initialBoxes =
                        SCREEN_WIDTH > SCREEN_HEIGHT
                        ? [
                            // Landscape: full height, half width
                            {
                                id: "1",
                                x: 0,
                                y: 0,
                                w: SCREEN_WIDTH / 2,
                                h: SCREEN_HEIGHT,
                                color: "tomato",
                            },
                            {
                                id: "2",
                                x: SCREEN_WIDTH / 2,
                                y: 0,
                                w: SCREEN_WIDTH / 2,
                                h: SCREEN_HEIGHT,
                                color: "skyblue",
                            },
                            ]
                        : [
                            // Portrait: full width, half height
                            {
                                id: "1",
                                x: 0,
                                y: 0,
                                w: SCREEN_WIDTH,
                                h: SCREEN_HEIGHT / 2,
                                color: "tomato",
                            },
                            {
                                id: "2",
                                x: 0,
                                y: SCREEN_HEIGHT / 2,
                                w: SCREEN_WIDTH,
                                h: SCREEN_HEIGHT / 2,
                                color: "skyblue",
                            },
                            ];
                    setBoxes(initialBoxes);
                    await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(initialBoxes));
                }
            } catch (e) {
                console.warn("Error loading boxes", e);
            }
        })();
    }, [SCREEN_WIDTH, SCREEN_HEIGHT]);

    // Save to storage
    const saveBoxes = useCallback(
        async (updated) => {
            try {
                setBoxes(updated);
                await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(updated));
            } catch (e) {
                console.log("Save error", e);
            }
        },
        [setBoxes]
    );

    const RenderBox = (box, index) => {
        const isEditing = editingBoxId === box.id;
        // PanResponder for dragging
        const panResponder = isEditing? PanResponder.create({
            onStartShouldSetPanResponder: () => true,
            onPanResponderMove: (_, gesture) => {
                const updated = [...boxes];
                updated[index] = {
                    ...box,
                    x: Math.min(SCREEN_WIDTH - box.w, Math.max(0, box.x + gesture.dx)),
                    y: Math.min(SCREEN_HEIGHT - box.h, Math.max(0, box.y + gesture.dy)),
                };
                setBoxes(updated);
            },
            onPanResponderRelease: (_, gesture) => {
                const updated = [...boxes];
                updated[index] = {
                    ...box,
                    x: Math.min(SCREEN_WIDTH - box.w, Math.max(0, box.x + gesture.dx)),
                    y: Math.min(SCREEN_HEIGHT - box.h, Math.max(0, box.y + gesture.dy)),
                };
                saveBoxes(updated);
            },
        }) : { panHandlers: {} };

        // PanResponder for resizing (bottom-right corner)
        const resizeResponder = isEditing? PanResponder.create({
            onStartShouldSetPanResponder: () => true,
            onPanResponderMove: (_, gesture) => {
                const updated = [...boxes];
                updated[index] = {
                    ...box,
                    w: Math.max(60, box.w + gesture.dx),
                    h: Math.max(60, box.h + gesture.dy),
                };
                setBoxes(updated);
            },
            onPanResponderRelease: (_, gesture) => {
                const updated = [...boxes];
                updated[index] = {
                    ...box,
                    w: Math.max(60, box.w + gesture.dx),
                    h: Math.max(60, box.h + gesture.dy),
                };
                saveBoxes(updated);
            },
        }) : { panHandlers: {} };

        return (
            <Pressable
                key={box.id}
                onLongPress={() => setEditingBoxId(box.id)}
                delayLongPress={300}
                style={[
                    styles.box,
                    {
                        backgroundColor: box.color,
                        left: box.x,
                        top: box.y,
                        width: box.w,
                        height: box.h,
                        borderWidth: isEditing ? 2 : 0,
                        borderColor: isEditing ? "#fff" : "transparent",
                    },
                ]}
            >
                {/* Resize Handle */}
                { isEditing && 
                    <View
                    {...resizeResponder.panHandlers}
                    style={[styles.resizeHandle, {bottom: 0, right: 0, borderTopLeftRadius: 8}]}
                    >
                        <MoveDiagonal2 size={scale(18)} color={'rgba(0,0,0,0.5)'} />
                    </View>
                }
                { isEditing && 
                    <View
                    {...panResponder.panHandlers}
                    style={[styles.resizeHandle, {bottom: 0, left: 0, borderTopRightRadius: 8}]}
                    >
                        <Move size={scale(18)} color={'rgba(0,0,0,0.5)'} />
                    </View>
                }
                { isEditing && 
                    <Pressable
                    onPress={() => setEditingBoxId(null)}
                    style={[styles.resizeHandle, {top: 0, right: 0, borderBottomLeftRadius: 8}]}
                    >
                        <Check size={scale(18)} color={'rgba(0,0,0,0.5)'} />
                    </Pressable>
                }
                { isEditing && 
                    <Pressable
                    onPress={() => setBoxes((prev) => {
                        const newArr = prev.filter((b) => b.id !== box.id);
                        saveBoxes(newArr);
                        return newArr
                    })}
                    style={[styles.resizeHandle, {top: 0, left: 0, borderBottomRightRadius: 8}]}
                    >
                        <Trash size={scale(18)} color={'rgba(0,0,0,0.5)'} />
                    </Pressable>
                }
            </Pressable>
        );
    };

    const addBoxAtCenter = () => {
        const boxWidth = 120;  // default width of new box
        const boxHeight = 120; // default height of new box

        const newBox = {
            id: Date.now().toString(),
            x: (SCREEN_WIDTH - boxWidth) / 2,
            y: (SCREEN_HEIGHT - boxHeight) / 2,
            w: boxWidth,
            h: boxHeight,
            color: "limegreen",
        };

        setBoxes((prev) => {
            saveBoxes([...prev, newBox]);
            return [...prev, newBox]
        });
    };

    return (
        <GestureHandlerRootView style={styles.container}>
            {boxes.map((box, index) => RenderBox(box, index))}
            {
                (editingBoxId || boxes.length === 0) &&
                <Pressable onPress={addBoxAtCenter} style={styles.addMore} >
                    <Plus size={scale(18)} color={'rgba(0,0,0,0.5)'} />
                </Pressable>
            }
        </GestureHandlerRootView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#111",
    },
    box: {
        position: "absolute",
    },
    resizeHandle: {
        width: 24,
        height: 24,
        backgroundColor: "rgba(255,255,255,0.8)",
        position: "absolute",
        justifyContent: 'center',
        alignItems: 'center'
    },
    addMore: {
        backgroundColor: "rgba(255,255,255,0.8)",
        padding: 8,
        alignSelf: 'flex-end',
        marginRight: 8
    }
});
