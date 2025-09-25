import AsyncStorage from "@react-native-async-storage/async-storage";
import { forwardRef, useCallback, useImperativeHandle, useState } from "react";
import { FlatList, Pressable, View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import { ToastMaker } from "../../components/ToastMaker";
import One from "./faces/One";
import Two from "./faces/Two";

const FacesMap = {
    'one': One,
    'two': Two
};

export const Controller = forwardRef(( { storeKey }, ref ) => {
    const [selector, setSelector] = useState(false);
    const [selectedFace, setSelectedFace] = useState('one');
    const FaceComponent = FacesMap[selectedFace];

    const changeFace = useCallback(
        async (val) => {
            try {
                const boxStr = await AsyncStorage.getItem(storeKey);
                if (boxStr) {
                    const box = JSON.parse(boxStr);
                    const updated = {
                        ...box,
                        selected: val
                    };
                    setSelectedFace(val);
                    setSelector(false);
                    await AsyncStorage.setItem(storeKey, JSON.stringify(updated));
                }
            } catch (e) {
                console.log("Save box error", e);
            }
        }, 
    [storeKey]);

    const handleTripleTap = () => {
        setSelector(true);
        ToastMaker('Scroll down and tap on any skin you like', 'long')
    }
    useImperativeHandle(ref, () => ({
        handleTripleTap
    }));

    return(
        <View style={styles.container}>
            {selector ? (
                <SelectionView changeFace={changeFace} />
            ) : (
                FaceComponent ? <FaceComponent /> : null
            )}
        </View>
    )
});

const SelectionView = ({ changeFace }) => {
    const faceKeys = Object.keys(FacesMap);
    const [containerHeight, setContainerHeight] = useState(0);
    const [containerWidth, setContainerWidth] = useState(0);

    const onLayout = (e) => {
        const { height, width } = e.nativeEvent.layout;
        setContainerHeight(height);
        setContainerWidth(width);
    };

    const RenderItem = ({ item }) => {
        const FaceComponent = FacesMap[item];
        return (
            <Pressable
                style={{
                    width: containerWidth,
                    height: containerHeight,
                    justifyContent: "center",
                    alignItems: "center",
                }}
                onPress={() => changeFace(item)}
            >
                <FaceComponent />
            </Pressable>
        );
    };

    return (
        <View style={{ flex: 1 }} onLayout={onLayout}>
            {containerHeight > 0 && (
                <FlatList
                    data={faceKeys}
                    keyExtractor={(item) => item}
                    renderItem={({ item }) => <RenderItem item={item} />}
                    pagingEnabled
                    showsVerticalScrollIndicator={false}
                    horizontal={false} // vertical scroll
                    snapToAlignment="center"
                    decelerationRate="fast"
                    getItemLayout={(_, index) => ({
                        length: containerHeight,
                        offset: containerHeight * index,
                        index,
                    })}
                />
            )}
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    selectorContainer: {
        flex: 1
    },
})