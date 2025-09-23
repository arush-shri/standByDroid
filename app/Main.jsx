import AsyncStorage from "@react-native-async-storage/async-storage";
import { Check, Move, MoveDiagonal2, Plus, Trash } from "lucide-react-native";
import { useCallback, useEffect, useRef, useState } from "react";
import {
    PanResponder,
    Pressable,
    useWindowDimensions,
    View
} from "react-native";
import { GestureHandlerRootView } from "react-native-gesture-handler";
import { scale, StyleSheet } from "react-native-size-scaling";
import SelectorView from "../components/SelectorView";
import EventsEmitter from "./context/EventsEmitter";

const RenderBox = ({ boxObj, storeKey, addBox, deleteBox }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [box, setBox] = useState(boxObj);
    const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = useWindowDimensions();
    const listenerRef = useRef(null);

    useEffect(() => {
        (async () => {
            try {
                const saved = await AsyncStorage.getItem(storeKey);
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
        EventsEmitter.emit('editingStarted', box.id); // Pass the id
    };

    useEffect(() => {
        listenerRef.current = (editingBoxId) => {
            if (editingBoxId !== box.id && isEditing) {
                setIsEditing(false);
            }
        };
        EventsEmitter.on('editingStarted', listenerRef.current);

        return () => {
            EventsEmitter.off('editingStarted', listenerRef.current);
        };
    }, [box.id, isEditing]);

    // Save to storage
    const saveBox = useCallback(
        async (updated) => {
            try {
                setBox(updated);
                await AsyncStorage.setItem(storeKey, JSON.stringify(updated));
            } catch (e) {
                console.log("Save box error", e);
            }
        }, 
    [setBox]);

    const saveBoxView = useCallback(
        async (val) => {
            try {
                const updated = {
                    ...box,
                    viewShow: val
                };
                setBox(updated)
                await AsyncStorage.setItem(storeKey, JSON.stringify(updated));
            } catch (e) {
                console.log("Save box error", e);
            }
        }, 
    [setBox, box]);

    // PanResponder for dragging
    const panResponder = PanResponder.create({
        onStartShouldSetPanResponder: () => true,
        onPanResponderMove: (_, gesture) => {
            const updated = {
                ...box,
                x: Math.min(SCREEN_WIDTH - box.w, Math.max(0, box.x + gesture.dx)),
                y: Math.min(SCREEN_HEIGHT - box.h, Math.max(0, box.y + gesture.dy)),
            };
            setBox(updated);
        },
        onPanResponderRelease: (_, gesture) => {
            const updated = {
                ...box,
                x: Math.min(SCREEN_WIDTH - box.w, Math.max(0, box.x + gesture.dx)),
                y: Math.min(SCREEN_HEIGHT - box.h, Math.max(0, box.y + gesture.dy)),
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

    if(!box) return null;

    return (
        <>
            <View
                key={box.id}
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
                    onPress={() => setIsEditing(false)}
                    style={[styles.resizeHandle, {top: 0, right: 0, borderBottomLeftRadius: 8}]}
                    >
                        <Check size={scale(18)} color={'rgba(0,0,0,0.5)'} />
                    </Pressable>
                }
                { isEditing && 
                    <Pressable
                    onPress={() => {
                        setBox(null);
                        deleteBox(storeKey);
                    }}
                    style={[styles.resizeHandle, {top: 0, left: 0, borderBottomRightRadius: 8}]}
                    >
                        <Trash size={scale(18)} color={'rgba(0,0,0,0.5)'} />
                    </Pressable>
                }
                <SelectorView startEditing={startEditing} viewSelected={box.viewShow || 'empty'}
                    isEditing={isEditing} viewChange={saveBoxView} />
            </View>
            {
                isEditing &&
                <Pressable onPress={addBox} style={styles.addMore} >
                    <Plus size={scale(25)} color={'rgba(0,0,0,0.5)'} style={{alignSelf: 'center'}} />
                </Pressable>
            }
        </>
    );
};

export default function Main() {
    const { width: SCREEN_WIDTH, height: SCREEN_HEIGHT } = useWindowDimensions();
    const [boxes, setBoxes] = useState([]);

    // Load from storage
    // useEffect(() => {
    //     (async () => {
    //         try {
    //             const saved = await AsyncStorage.getItem(STORAGE_KEY);
    //             if (saved) {
    //                 setBoxes(JSON.parse(saved));
    //             } else {
    //                 // No storage, define initial boxes based on orientation
    //                 const initialBoxes =
    //                     SCREEN_WIDTH > SCREEN_HEIGHT
    //                     ? [
    //                         // Landscape: full height, half width
    //                         {
    //                             id: "1",
    //                             x: 0,
    //                             y: 0,
    //                             w: SCREEN_WIDTH / 2,
    //                             h: SCREEN_HEIGHT,
    //                             color: "tomato",
    //                         },
    //                         {
    //                             id: "2",
    //                             x: SCREEN_WIDTH / 2,
    //                             y: 0,
    //                             w: SCREEN_WIDTH / 2,
    //                             h: SCREEN_HEIGHT,
    //                             color: "skyblue",
    //                         },
    //                         ]
    //                     : [
    //                         // Portrait: full width, half height
    //                         {
    //                             id: "1",
    //                             x: 0,
    //                             y: 0,
    //                             w: SCREEN_WIDTH,
    //                             h: SCREEN_HEIGHT / 2,
    //                             color: "tomato",
    //                         },
    //                         {
    //                             id: "2",
    //                             x: 0,
    //                             y: SCREEN_HEIGHT / 2,
    //                             w: SCREEN_WIDTH,
    //                             h: SCREEN_HEIGHT / 2,
    //                             color: "skyblue",
    //                         },
    //                         ];
    //                 setBoxes(initialBoxes);
    //                 await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(initialBoxes));
    //             }
    //         } catch (e) {
    //             console.warn("Error loading boxes", e);
    //         }
    //     })();
    // }, [SCREEN_WIDTH, SCREEN_HEIGHT]);

    const initFunc = async() => {
        const keys = await AsyncStorage.getAllKeys();
        const filteredKeys = keys.filter((key) => key.startsWith('boxPos_'));

        if (filteredKeys.length === 0) return;

        const stores = await AsyncStorage.multiGet(filteredKeys);
        setBoxes(
            stores.map(([_, value]) => {
                try {
                    return JSON.parse(value);
                } catch {
                    return value;
                }
            })
        );
    }

    const saveBoxes = useCallback(
        async (storeKey, updated) => {
            try {
                await AsyncStorage.setItem(storeKey, JSON.stringify(updated));
            } catch (e) {
                console.log("Save box error", e);
            }
        }, 
    [setBoxes]);

    const addBoxAtCenter = () => {
        if(boxes.length > 6){
            return
        }
        const boxWidth = scale(120);
        const boxHeight = scale(120);
        const id = Date.now().toString()

        const newBox = {
            id: id,
            x: (SCREEN_WIDTH - boxWidth) / 2,
            y: (SCREEN_HEIGHT - boxHeight) / 2,
            w: boxWidth,
            h: boxHeight,
            color: "limegreen",
            storeKey: `boxPos_${id}`,
            viewShow: 'empty'
        };

        setBoxes((prev) => {
            saveBoxes(`boxPos_${id}`, newBox);
            return [...prev, newBox]
        });
    };

    const deleteBox = useCallback((storeKey) => {
        AsyncStorage.removeItem(storeKey);
        setBoxes((prev) => prev.filter((box) => box.storeKey !== storeKey));
    },[setBoxes])

    useEffect(() => {
        initFunc();
    }, [])

    return (
        <GestureHandlerRootView style={styles.container}>
            {boxes?.map((box, index) => <RenderBox boxObj={box} storeKey={box.storeKey} key={index} 
                addBox={addBoxAtCenter} deleteBox={deleteBox} />)}
            {
                (boxes.length === 0) &&
                <Pressable onPress={addBoxAtCenter} style={styles.addMore} >
                    <Plus size={scale(25)} color={'rgba(0,0,0,0.5)'} style={{alignSelf: 'center'}} />
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
        marginLeft: 8,
        position: "relative",
        top: 10,
        left: 10,
        width: 30,
        height: 30,
        justifyContent: 'center'
    }
});
