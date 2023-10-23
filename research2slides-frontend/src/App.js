import { Flex, Heading, useToast, Image } from "@chakra-ui/react";
import { useState } from "react";
import { ChakraProvider, CSSReset } from "@chakra-ui/react";
import DesignSelector from "./components/design";
import ConvertButton from "./components/convert";

function App() {
  const [design, setDesign] = useState(null);
  const toast = useToast();

  const handleConversion = () => {
    // same logic as before
  };

  return (
    <ChakraProvider>
      <CSSReset />
      <Flex direction="column" align="center" m={4}>
        <Flex align="center" my={5}>
          <Image src="./logo.png" alt="Research2Slides Logo" boxSize="70px" mr={3} />
          <Heading>Research2Slides</Heading>
        </Flex>
        <DesignSelector design={design} setDesign={setDesign} />
        <ConvertButton onConvert={handleConversion} />
      </Flex>
    </ChakraProvider>
  );
}

export default App;