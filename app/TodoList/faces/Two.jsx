import { Text, View } from "react-native";
import { StyleSheet } from "react-native-size-scaling";

const Two = ({  }) => {
    return(
        <View style={styles.contatiner}>
            <Text style={{fontSize: 18, color: 'red'}}>TWOOOOOOOOOOOOOOOOO</Text>
        </View>
    )
}

const styles = StyleSheet.create({
    contatiner: {
        flex: 1,
        backgroundColor: '#000'
    }
});

export default Two;