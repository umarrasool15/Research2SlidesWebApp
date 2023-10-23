import { Box, Grid, Flex, Image, useRadio, UseRadioProps, Text, Button, Spinner, Icon, Tooltip, useDisclosure } from "@chakra-ui/react";
import { FiRefreshCw } from "react-icons/fi";

const ConvertButton = ({ onConvert }) => {
  const { isOpen, onOpen, onClose } = useDisclosure(); // To handle showing of spinner
  const handleClick = async () => {
    onOpen();
    await onConvert();
    onClose();
  };

  return (
    <Tooltip label="Click to start the conversion" placement="right">
      <Box position="relative">
        <Button
          leftIcon={<Icon as={FiRefreshCw} />} // Add an icon
          colorScheme="green"
          onClick={handleClick}
          mt={5}
          _hover={{
            bgColor: "green.600" // Darker shade on hover
          }}
        >
          Convert
        </Button>
        {isOpen && ( // Display spinner on top of the button when converting
          <Box position="absolute" top="50%" left="50%" transform="translate(-50%, -50%)">
            <Spinner />
          </Box>
        )}
      </Box>
    </Tooltip>
  );
};

export default ConvertButton;
