package org.jetbrains.kotlin.backend.konan.serialization

import org.jetbrains.kotlin.backend.common.LoggingContext
import org.jetbrains.kotlin.backend.common.serialization.*
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.descriptors.IrBuiltIns
import org.jetbrains.kotlin.ir.symbols.IrSymbol

private class KonanDeclarationTable(globalDeclarationTable: GlobalDeclarationTable) :
        DeclarationTable(globalDeclarationTable) {

    /**
     * It is incorrect to compute UniqId for declarations from metadata-based libraries.
     * Instead we should get precomputed value from metadata.
     */
//    override fun tryComputeBackendSpecificUniqId(declaration: IrDeclaration): UniqId? {
//        return if (declaration.descriptor.module.isFromInteropLibrary()) {
//            // Property accessor doesn't provide UniqId so we need to get it from the property itself.
//            UniqId(declaration.descriptor.propertyIfAccessor.getUniqId() ?: error("No uniq id found for ${declaration.descriptor}"))
//        } else {
//            null
//        }
//    }
}

class KonanIrModuleSerializer(
    logger: LoggingContext,
    irBuiltIns: IrBuiltIns,
    private val expectDescriptorToSymbol: MutableMap<DeclarationDescriptor, IrSymbol>,
    val skipExpects: Boolean
) : IrModuleSerializer<KonanIrFileSerializer>(logger) {


    private val globalDeclarationTable = KonanGlobalDeclarationTable(irBuiltIns)

    override fun createSerializerForFile(file: IrFile): KonanIrFileSerializer =
            KonanIrFileSerializer(logger, KonanDeclarationTable(globalDeclarationTable), expectDescriptorToSymbol, skipExpects = skipExpects)
}
