# Flashcards

## Serialization and deserialization

I started out using the ObjectOutputStream class to serialize data to file. But I ran into some issues when I tried to deserialize a file that was serialized with a different version of code. These issues were caused by changes to the data structure. For this reason I decided to save the deck as an XML file.
