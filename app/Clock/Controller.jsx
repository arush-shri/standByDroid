import { View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";

export const Controller = () => {
    return(
        <View style={styles.container}></View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'red'
    },
})