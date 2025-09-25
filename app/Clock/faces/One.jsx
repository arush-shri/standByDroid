import { memo } from "react";
import { Text } from "react-native";
import { StyleSheet } from "react-native-size-scaling";
import DriftingView from "../../../components/DriftingView";

const One = memo(({ time }) => {
    return (
        <DriftingView>
            <Text style={styles.text}>{time.toLocaleTimeString()}</Text>
        </DriftingView>
    );
});

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#000",
    },
    text: {
        fontSize: 18,
        color: "red",
    },
});

export default One;
